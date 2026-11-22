package bw.co.knowvera.organisation.client;

import java.time.Duration;
import bw.co.knowvera.retention.RecordRetention;
import bw.co.knowvera.utils.RequestTokens;
import bw.co.knowvera.PhoneNumber;
import bw.co.knowvera.PhoneType;
import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.SortOrderFactory;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentMapper;
import bw.co.knowvera.document.DocumentRepository;
import bw.co.knowvera.individual.EmploymentStatus;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.MaritalStatus;
import bw.co.knowvera.individual.IndividualIdentityType;
import bw.co.knowvera.individual.IndividualMapper;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.individual.Sex;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.messaging.ClientRequestNotification;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.sequence.SequenceGenerator;
import bw.co.knowvera.sequence.SequenceGeneratorRepository;
import bw.co.knowvera.sequence.SequenceGeneratorService;
import bw.co.knowvera.sequence.SequencePart;
import bw.co.knowvera.sequence.SequencePartType;
import bw.co.knowvera.settings.SettingsMapper;
import bw.co.knowvera.settings.SettingsRepository;
import io.micrometer.common.util.StringUtils;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @see bw.co.knowvera.organisation.client.ClientRequestService
 */
@Service("clientRequestService")
public class ClientRequestServiceImpl
        extends ClientRequestServiceBase {

    @Value("${app.request-token-length}")
    private int requestTokenLength;

    /** Lifetime of the emailed client request link (account request token). */
    @Value("${app.request-token-link-ttl:7d}")
    private Duration requestLinkTtl;

    /** Lifetime of the identity confirmation and registration tokens minted from that link. */
    @Value("${app.request-token-session-ttl:24h}")
    private Duration requestSessionTtl;

     private static final String SEQUENCE_NAME = "CLIENT_REQUEST_REF";

    private final PasswordEncoder passwordEncoder;
    private final ClientRequestNotification clientRequestNotification;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final OrganisationRepository organisationRepository;
    private final RecordRetention recordRetention;
    private final TransactionOperations transaction;

    /** The constructor Spring uses; the other one lets tests run without a transaction manager. */
    @Autowired
    public ClientRequestServiceImpl(ClientRequestRepository clientRequestRepository,
            ClientRequestMapper clientRequestMapper,PasswordEncoder passwordEncoder,
            ClientRequestNotification clientRequestNotification, IndividualRepository individualRepository,
            IndividualMapper individualMapper, OrganisationRepository organisationRepository,
            SequenceGeneratorRepository sequenceGeneratorRepository, SequenceGeneratorService sequenceGeneratorService,
            DocumentRepository documentRepository, DocumentMapper documentMapper,
            SettingsRepository settingsRepository, SettingsMapper settingsMapper,
            RecordRetention recordRetention, PlatformTransactionManager transactionManager) {
        this(clientRequestRepository, clientRequestMapper, passwordEncoder, clientRequestNotification,
                individualRepository, individualMapper, organisationRepository, sequenceGeneratorRepository,
                sequenceGeneratorService, documentRepository, documentMapper, settingsRepository, settingsMapper,
                recordRetention, new TransactionTemplate(transactionManager));
    }

    ClientRequestServiceImpl(ClientRequestRepository clientRequestRepository,
            ClientRequestMapper clientRequestMapper,PasswordEncoder passwordEncoder,
            ClientRequestNotification clientRequestNotification, IndividualRepository individualRepository,
            IndividualMapper individualMapper, OrganisationRepository organisationRepository,
            SequenceGeneratorRepository sequenceGeneratorRepository, SequenceGeneratorService sequenceGeneratorService,
            DocumentRepository documentRepository, DocumentMapper documentMapper,
            SettingsRepository settingsRepository, SettingsMapper settingsMapper,
            RecordRetention recordRetention, TransactionOperations transaction) {
        super(clientRequestRepository, clientRequestMapper, individualRepository,
                individualMapper, documentRepository, documentMapper, settingsRepository,
                settingsMapper);
        this.passwordEncoder = passwordEncoder;
        this.clientRequestNotification = clientRequestNotification;
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.organisationRepository = organisationRepository;
        this.recordRetention = recordRetention;
        this.transaction = transaction;
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#findById(String)
     */
    @Override
    protected ClientRequestDTO handleFindById(String id)
            throws Exception {

        ClientRequest clientRequest = clientRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ClientRequestServiceException("ClientRequest not found"));

        ClientRequestDTO clientRequestDTO = clientRequestMapper.toClientRequestDTO(clientRequest);
        this.updateClientName(Arrays.asList(clientRequestDTO));
        return clientRequestDTO;
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#save(ClientRequestDTO)
     */
    @Override
    protected ClientRequestDTO handleSave(ClientRequestDTO clientRequest)
            throws Exception {

        ClientRequest clientRequestEntity = clientRequestMapper.clientRequestDTOToEntity(clientRequest);

        if (StringUtils.isBlank(clientRequestEntity.getRef())) {

            clientRequestEntity.setRef(nextReference(clientRequestEntity.getOrganisation()));
        }

        boolean isNew = StringUtils.isBlank(clientRequest.getId());
        String token = null;

        if (isNew) {
            // Generate a random token with letters, digits and special characters
            token = RequestTokens.issue(requestTokenLength);

            // Encode the token
            String encodedToken = passwordEncoder.encode(token);
            clientRequestEntity.setAccountRequestToken(encodedToken);
        }

        clientRequestEntity = clientRequestRepository.save(clientRequestEntity);

        if (isNew) {

            // For new requests, we might want to send the token via email or other means
            // For this example, we'll just print it to the console (not recommended for
            // production)

            clientRequestNotification.queueEmailNotificationsForRequests(
                    Arrays.asList(clientRequestEntity),
                    Map.of(clientRequestEntity.getTargetId(), token),
                    clientRequest.getOrganisation());

        }

        ClientRequestDTO clientRequestDTO = clientRequestMapper.toClientRequestDTO(clientRequestEntity);
        this.updateClientName(Arrays.asList(clientRequestDTO));
        return clientRequestDTO;
    }

    /** The organisation's next client request reference (<CODE>_KR-00000001), creating its sequence on first use. */
    private String nextReference(Organisation organisation) {

        StringBuilder sb = new StringBuilder(organisation.getCode());
        sb.append('_').append(SEQUENCE_NAME);

        SequenceGenerator sequenceGenerator = sequenceGeneratorRepository.findByName(sb.toString()).orElse(null);

        if (sequenceGenerator == null) {

            sequenceGenerator = new SequenceGenerator();
            sequenceGenerator.setName(sb.toString());
            sequenceGenerator.setTargetEntity(TargetEntity.CLIENT_REQUEST);
        }

        // Liquibase seeds the generator (db/sequence/db.sequence.xml); its parts are added on first use
        if (CollectionUtils.isEmpty(sequenceGenerator.getSequenceParts())) {

            List<SequencePart> sequenceParts = sequenceGenerator.getSequenceParts() == null ? new ArrayList<>()
                    : sequenceGenerator.getSequenceParts();

            SequencePart counterPart = new SequencePart();
            counterPart.setPosition(0);
            counterPart.setType(SequencePartType.STATIC);
            counterPart.setInitialValue(organisation.getCode() + '_' + "KR-");
            counterPart.setName(sb.toString() + "_PREFIX");
            counterPart.setSequenceGenerator(sequenceGenerator);
            sequenceParts.add(counterPart);

            counterPart = new SequencePart();
            counterPart.setPosition(1);
            counterPart.setType(SequencePartType.COUNTER);
            counterPart.setName(sb.toString() + "_COUNTER");
            counterPart.setInitialValue("00000000");
            counterPart.setSequenceGenerator(sequenceGenerator);
            sequenceParts.add(counterPart);

            sequenceGenerator.setSequenceParts(sequenceParts);
            sequenceGeneratorRepository.save(sequenceGenerator);
        }

        return sequenceGeneratorService.generateNextSequenceValue(sb.toString(), true);
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        if (clientRequestRepository.existsById(UUID.fromString(id))) {
            clientRequestRepository.deleteById(UUID.fromString(id));
            return true;
        }

        throw new ClientRequestServiceException("ClientRequest not found");
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#getAll()
     */
    @Override
    protected List<ClientRequestDTO> handleGetAll()
            throws Exception {

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(clientRequestRepository.findAll());
        this.updateClientName(dtos);

        return dtos;
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#search(ClientRequestSearchCriteria,
     *      Set<PropertySearchOrder>)
     */
    @Override
    protected List<ClientRequestDTO> handleSearch(ClientRequestSearchCriteria criteria,
            Set<PropertySearchOrder> sortProperties)
            throws Exception {

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);

        Sort sort = SortOrderFactory.createSortOrder(sortProperties);

        Collection<ClientRequest> requests = (sort != null)
                ? clientRequestRepository.findAll(spec, sort)
                : clientRequestRepository.findAll(spec);

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;

    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<ClientRequestDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ClientRequest> requests = clientRequestRepository.findAll(pageRequest);

        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#search(SearchObject<ClientRequestSearchCriteria>)
     */
    @Override
    protected Page<ClientRequestDTO> handleSearch(SearchObject<ClientRequestSearchCriteria> criteria)
            throws Exception {

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria.getCriteria());
        Sort sort = SortOrderFactory.createSortOrder(criteria.getSortings());

        Integer pageNumber = criteria.getPageNumber() != null ? criteria.getPageNumber() : 0;
        Integer pageSize = criteria.getPageSize() != null ? criteria.getPageSize() : 10;

        PageRequest pageRequest = (sort != null)
                ? PageRequest.of(pageNumber, pageSize, sort)
                : PageRequest.of(pageNumber, pageSize);
        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, pageRequest);

        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    private Specification<ClientRequest> buildSpecificationFromCriteria(ClientRequestSearchCriteria criteria) {

        Specification<ClientRequest> spec = ((root, query, builder) -> builder.conjunction());

        if (criteria == null) {
            return spec;
        }

        if (StringUtils.isNotBlank(criteria.getOrganisationId())) {

            spec = spec.and(
                    (root, query, cb) -> cb.equal(root.get("organisation").get("id"),
                            UUID.fromString(criteria.getOrganisationId())));
        }

        if (criteria.getTarget() != null) {

            spec = spec.and((root, query, cb) -> cb.equal(root.get("target"), criteria.getTarget()));
        }

        if (StringUtils.isNotBlank(criteria.getTargetId())) {

            spec = spec.and((root, query, cb) -> cb.equal(root.get("targetId"), criteria.getTargetId()));
        }

        if (CollectionUtils.isNotEmpty(criteria.getStatuses())) {

            spec = spec.and((root, query, cb) -> root.get("status").in(criteria.getStatuses()));

        }

        return spec;
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#findByOrganisation(String)
     */
    @Override
    protected List<ClientRequestDTO> handleFindByOrganisation(String organisationId)
            throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setOrganisationId(organisationId);
        // criteria.setStatus(ClientRequestStatus.);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        List<ClientRequest> requests = clientRequestRepository.findAll(spec);

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#findByOrganisation(String,
     *      Integer, Integer)
     */
    @Override
    protected Page<ClientRequestDTO> handleFindByOrganisation(String organisationId, Integer pageNumber,
            Integer pageSize)
            throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setOrganisationId(organisationId);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));

        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#findByStatus(ClientRequestStatus)
     */
    @Override
    protected List<ClientRequestDTO> handleFindByStatus(ClientRequestStatus status)
            throws Exception {

        Specification<ClientRequest> spec = (root, query, cb) -> cb.equal(root.get("status"), status);

        List<ClientRequest> requests = clientRequestRepository.findAll(spec);

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    /**
     * Gives each unsaved request (no id yet) a new account request token, stores its hash on the
     * request and returns the plain tokens by target id. Saved requests keep theirs.
     */
    static Map<String, String> issueTokensForNewRequests(List<ClientRequest> clientRequests, int tokenLength,
            PasswordEncoder passwordEncoder) {

        Map<String, String> tokens = new HashMap<>();
        for (ClientRequest c : clientRequests) {
            if (c.getId() != null) {
                continue;
            }
            String token = RequestTokens.issue(tokenLength);
            c.setAccountRequestToken(passwordEncoder.encode(token));
            tokens.put(c.getTargetId(), token);
        }
        return tokens;
    }

    /**
     * Imports individual clients from a spreadsheet (.xlsx, .xls or .csv) laid out like
     * templates/client-request-template.csv. Each row becomes one PENDING client request of the
     * organisation, with a reference, a token and an invitation exactly as {@link #handleSave}
     * gives a single request. The individual with the row's identity type and number is reused
     * when one is stored (its stored details are kept); otherwise a new one is created from the row.
     *
     * <p>The import is all-or-nothing. Every row is checked first; if any is wrong a
     * {@link ClientRequestImportException} naming each wrong row is thrown and nothing is stored.
     * Storing happens in one transaction, and the invitations are queued after it commits.
     *
     * <p>Only {@code target=INDIVIDUAL} is supported: organisation clients need far more than a
     * row can hold and are created one at a time.
     *
     * @param document the uploaded file's record, not saved yet; saved here with the requests
     * @see bw.co.knowvera.organisation.client.ClientRequestService#uploadRequests(InputStream)
     */
    @Override
    protected Page<ClientRequestDTO> handleUploadRequests(InputStream inputStream, String user,
            String organisationId, DocumentDTO document, TargetEntity target, String organisation)
            throws Exception {

        if (target != TargetEntity.INDIVIDUAL) {
            throw new ClientRequestImportException(
                    "Only individual clients can be imported from a spreadsheet. Create organisation client requests one at a time.");
        }

        List<ClientRequestSpreadsheet.Row> rows = ClientRequestSpreadsheet.read(inputStream.readAllBytes());
        if (rows.isEmpty()) {
            throw new ClientRequestImportException("The file has no client rows below its header row.");
        }
        if (rows.size() > MAX_IMPORT_ROWS) {
            throw new ClientRequestImportException("The file has " + rows.size() + " rows; at most "
                    + MAX_IMPORT_ROWS + " clients can be imported at a time.");
        }
        List<String> missingColumns = IMPORT_MANDATORY_COLUMNS.entrySet().stream()
                .filter(column -> rows.stream().noneMatch(row -> row.values().containsKey(column.getKey())))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        if (!missingColumns.isEmpty()) {
            throw new ClientRequestImportException("The file has no values for the mandatory column(s) "
                    + String.join(", ", missingColumns) + ". Download the template for the expected layout.");
        }

        return transaction.execute(status -> importIndividuals(rows, user, organisationId, document, organisation));
    }

    private static final int MAX_IMPORT_ROWS = 1000;

    /** Columns every row needs, with the names the template gives them. */
    private static final Map<String, String> IMPORT_MANDATORY_COLUMNS = importColumns(
            ClientRequestSpreadsheet.FIRST_NAME, "First Name",
            ClientRequestSpreadsheet.SURNAME, "Surname",
            ClientRequestSpreadsheet.IDENTITY_TYPE, "Identity Type",
            ClientRequestSpreadsheet.IDENTITY_NO, "Identity No",
            ClientRequestSpreadsheet.EMAIL, "Email Address");

    /** Columns a row also needs when its individual is not stored yet: an individual cannot be saved without them. */
    private static final Map<String, String> IMPORT_NEW_INDIVIDUAL_COLUMNS = importColumns(
            ClientRequestSpreadsheet.DATE_OF_BIRTH, "Date of Birth",
            ClientRequestSpreadsheet.NATIONALITY, "Nationality",
            ClientRequestSpreadsheet.MARITAL_STATUS, "Marital Status",
            ClientRequestSpreadsheet.EMPLOYMENT_STATUS, "Employment Status");

    private static Map<String, String> importColumns(String... keysAndLabels) {
        Map<String, String> columns = new LinkedHashMap<>();
        for (int i = 0; i < keysAndLabels.length; i += 2) {
            columns.put(keysAndLabels[i], keysAndLabels[i + 1]);
        }
        return columns;
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private static final Map<String, IndividualIdentityType> IDENTITY_TYPES = Map.ofEntries(
            Map.entry("OMANG", IndividualIdentityType.OMANG),
            Map.entry("NATIONAL_ID", IndividualIdentityType.OMANG),
            Map.entry("PASSPORT", IndividualIdentityType.PASSPORT),
            Map.entry("RESIDENCE_PERMIT", IndividualIdentityType.RESIDENCE_PERMIT),
            Map.entry("BIRTH_CERTIFICATE", IndividualIdentityType.BIRTH_CERTIFICATE),
            Map.entry("REFUGEE_ID", IndividualIdentityType.REFUGEE_ID),
            Map.entry("DRIVERS_LICENCE", IndividualIdentityType.DRIVERS_LICENCE),
            Map.entry("DRIVERS_LICENSE", IndividualIdentityType.DRIVERS_LICENCE),
            Map.entry("DRIVER_LICENCE", IndividualIdentityType.DRIVERS_LICENCE),
            Map.entry("DRIVER_LICENSE", IndividualIdentityType.DRIVERS_LICENCE),
            Map.entry("DRIVING_LICENCE", IndividualIdentityType.DRIVERS_LICENCE),
            Map.entry("DRIVING_LICENSE", IndividualIdentityType.DRIVERS_LICENCE));

    private static final Map<String, Sex> SEXES = Map.of(
            "M", Sex.MALE, "MALE", Sex.MALE, "F", Sex.FEMALE, "FEMALE", Sex.FEMALE,
            "OTHER", Sex.OTHER, "UNSPECIFIED", Sex.UNSPECIFIED);

    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d-M-uuuu").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ENGLISH).withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.ENGLISH).withResolverStyle(ResolverStyle.STRICT));

    /** A row that passed every check: the individual it is for (stored or new) and whether to save it. */
    private record ImportedClient(Individual individual, boolean save, boolean reopened) {
    }

    /** Checks every row, then stores the file's record, the new individuals and the requests. */
    private Page<ClientRequestDTO> importIndividuals(List<ClientRequestSpreadsheet.Row> rows, String user,
            String organisationId, DocumentDTO document, String organisationName) {

        Organisation organisation = organisationRepository.findById(UUID.fromString(organisationId))
                .orElseThrow(() -> new ClientRequestImportException("The organisation could not be found."));

        List<String> errors = new ArrayList<>();
        List<ImportedClient> clients = new ArrayList<>();
        Map<String, Integer> rowByIdentity = new HashMap<>();

        for (ClientRequestSpreadsheet.Row row : rows) {
            List<String> rowErrors = new ArrayList<>();
            ImportedClient client = checkRow(row, organisation, rowByIdentity, user, rowErrors);
            if (rowErrors.isEmpty()) {
                clients.add(client);
            } else {
                errors.add("Row " + row.number() + ": " + String.join("; ", rowErrors));
            }
        }

        if (!errors.isEmpty()) {
            throw new ClientRequestImportException(errors.size() + " of " + rows.size()
                    + " rows could not be imported. Nothing was imported; correct the rows and upload the file again.",
                    errors);
        }

        Document file = documentMapper.documentDTOToEntity(document);
        file.setTarget(TargetEntity.CLIENT_REQUEST);
        if (file.getCreatedAt() == null) {
            file.setCreatedAt(LocalDateTime.now());
            file.setCreatedBy(user);
        }
        file = documentRepository.save(file);

        List<ClientRequest> requests = new ArrayList<>();
        for (ImportedClient client : clients) {

            Individual individual = client.save() ? individualRepository.save(client.individual())
                    : client.individual();
            if (client.reopened()) {
                recordRetention.reopenKycRecords(TargetEntity.INDIVIDUAL, individual.getId().toString());
            }

            ClientRequest request = new ClientRequest();
            request.setOrganisation(organisation);
            request.setTarget(TargetEntity.INDIVIDUAL);
            request.setTargetId(individual.getId().toString());
            request.setStatus(ClientRequestStatus.PENDING);
            request.setRef(nextReference(organisation));
            request.setDocument(file);
            request.setCreatedBy(user);
            request.setCreatedAt(LocalDateTime.now());
            requests.add(request);
        }

        Map<String, String> tokens = issueTokensForNewRequests(requests, requestTokenLength, passwordEncoder);
        requests = clientRequestRepository.saveAll(requests);

        // Sent once this transaction has committed
        clientRequestNotification.queueEmailNotificationsForRequests(requests, tokens, organisationName);

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);
        return new PageImpl<>(dtos);
    }

    /**
     * Checks one row, adding what is wrong with it to {@code errors}. Returns the individual the
     * row is for when nothing is wrong. Stores nothing.
     */
    private ImportedClient checkRow(ClientRequestSpreadsheet.Row row, Organisation organisation,
            Map<String, Integer> rowByIdentity, String user, List<String> errors) {

        IMPORT_MANDATORY_COLUMNS.forEach((column, label) -> {
            if (row.get(column) == null) {
                errors.add(label + " is required");
            }
        });

        String email = row.get(ClientRequestSpreadsheet.EMAIL);
        if (email != null && !EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Email Address '" + email + "' is not a valid email address");
        }

        IndividualIdentityType identityType = lookUp(row, ClientRequestSpreadsheet.IDENTITY_TYPE, "Identity Type",
                IDENTITY_TYPES, IndividualIdentityType.values(), errors);
        Sex sex = lookUp(row, ClientRequestSpreadsheet.SEX, "Sex", SEXES, Sex.values(), errors);
        MaritalStatus maritalStatus = lookUp(row, ClientRequestSpreadsheet.MARITAL_STATUS, "Marital Status",
                Map.of(), MaritalStatus.values(), errors);
        EmploymentStatus employmentStatus = lookUp(row, ClientRequestSpreadsheet.EMPLOYMENT_STATUS,
                "Employment Status", Map.of(), EmploymentStatus.values(), errors);
        LocalDate dateOfBirth = dateOfBirth(row, errors);

        String identityNo = row.get(ClientRequestSpreadsheet.IDENTITY_NO);
        if (identityType == null || identityNo == null) {
            return null;
        }

        Integer earlierRow = rowByIdentity.putIfAbsent(identityNo.toLowerCase(Locale.ROOT), row.number());
        if (earlierRow != null) {
            errors.add("Identity No " + identityNo + " is already on row " + earlierRow);
            return null;
        }

        Individual stored = individualRepository.findByIdentityNoAndIdentityType(identityNo, identityType)
                .orElse(null);

        if (stored == null) {

            // An identity number belongs to one individual, whatever its type
            Individual sameNumber = individualRepository.findByIdentityNo(identityNo).orElse(null);
            if (sameNumber != null) {
                errors.add("Identity No " + identityNo + " is already registered with identity type "
                        + sameNumber.getIdentityType());
            }
            IMPORT_NEW_INDIVIDUAL_COLUMNS.forEach((column, label) -> {
                if (row.get(column) == null) {
                    errors.add(label + " is required for a client who is not registered yet");
                }
            });
            if (!errors.isEmpty()) {
                return null;
            }

            Individual individual = new Individual();
            individual.setFirstName(row.get(ClientRequestSpreadsheet.FIRST_NAME));
            individual.setMiddleName(row.get(ClientRequestSpreadsheet.MIDDLE_NAME));
            individual.setSurname(row.get(ClientRequestSpreadsheet.SURNAME));
            individual.setIdentityType(identityType);
            individual.setIdentityNo(identityNo);
            individual.setEmailAddress(email);
            individual.setPhoneNumbers(phoneNumbers(row.get(ClientRequestSpreadsheet.PHONES)));
            individual.setPhysicalAddress(row.get(ClientRequestSpreadsheet.PHYSICAL_ADDRESS));
            individual.setPostalAddress(row.get(ClientRequestSpreadsheet.POSTAL_ADDRESS));
            individual.setSex(sex);
            individual.setDateOfBirth(dateOfBirth);
            individual.setNationality(row.get(ClientRequestSpreadsheet.NATIONALITY));
            individual.setMaritalStatus(maritalStatus);
            individual.setEmploymentStatus(employmentStatus);
            individual.setKycStatus(KycComplianceStatus.ABSENT);
            individual.setCreatedAt(LocalDateTime.now());
            individual.setCreatedBy(user);
            return new ImportedClient(individual, true, false);
        }

        Specification<ClientRequest> sameClient = (root, query, cb) -> cb.and(
                cb.equal(root.get("organisation").get("id"), organisation.getId()),
                cb.equal(root.get("target"), TargetEntity.INDIVIDUAL),
                cb.equal(root.get("targetId"), stored.getId().toString()));
        if (clientRequestRepository.count(sameClient) > 0) {
            errors.add("there is already a client request for " + identityType + " " + identityNo
                    + " from this organisation");
        }
        if (!errors.isEmpty()) {
            return null;
        }

        // The stored individual's details are theirs: a row only supplies a missing email address
        boolean changed = false;
        if (StringUtils.isBlank(stored.getEmailAddress())) {
            stored.setEmailAddress(email);
            changed = true;
        }

        // A returning customer starts a new relationship on their retained records
        boolean reopened = stored.getClosedAt() != null;
        if (reopened) {
            stored.setClosedAt(null);
            stored.setRetainUntil(null);
            changed = true;
        }
        if (changed) {
            stored.setModifiedAt(LocalDateTime.now());
            stored.setModifiedBy(user);
        }
        return new ImportedClient(stored, changed, reopened);
    }

    /**
     * The constant a cell names ("Driver's Licence", "self employed"), by alias or by constant
     * name; null with an error for an unknown value, null alone for a blank cell.
     */
    private static <E extends Enum<E>> E lookUp(ClientRequestSpreadsheet.Row row, String column, String label,
            Map<String, E> aliases, E[] constants, List<String> errors) {

        String value = row.get(column);
        if (value == null) {
            return null;
        }
        String key = value.toUpperCase(Locale.ROOT).replaceAll("['’`]", "").replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("^_|_$", "");
        E match = aliases.get(key);
        for (E constant : constants) {
            if (match == null && constant.name().equals(key)) {
                match = constant;
            }
        }
        if (match == null) {
            errors.add(label + " '" + value + "' is not recognised (use one of "
                    + Arrays.stream(constants).map(Enum::name).collect(Collectors.joining(", ")) + ")");
        }
        return match;
    }

    private static LocalDate dateOfBirth(ClientRequestSpreadsheet.Row row, List<String> errors) {

        String value = row.get(ClientRequestSpreadsheet.DATE_OF_BIRTH);
        if (value == null) {
            return null;
        }
        for (DateTimeFormatter format : DATE_FORMATS) {
            try {
                LocalDate date = LocalDate.parse(value, format);
                if (date.isAfter(LocalDate.now())) {
                    errors.add("Date of Birth " + value + " is in the future");
                    return null;
                }
                return date;
            } catch (DateTimeParseException e) {
                // try the next format
            }
        }
        errors.add("Date of Birth '" + value + "' is not a date (use YYYY-MM-DD or DD/MM/YYYY)");
        return null;
    }

    /** Several numbers in one cell are separated by ';' or ','. */
    private static List<PhoneNumber> phoneNumbers(String cell) {

        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        if (cell != null) {
            for (String number : cell.split("[;,]")) {
                if (!number.isBlank()) {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setType(PhoneType.OTHER);
                    phoneNumber.setPhoneNumber(number.trim());
                    phoneNumbers.add(phoneNumber);
                }
            }
        }
        return phoneNumbers;
    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#findByIndividual(String)
     */
    @Override
    protected List<ClientRequestDTO> handleFindByIndividual(String individualId)
            throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setTargetId(individualId);
        criteria.setTarget(TargetEntity.INDIVIDUAL);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        List<ClientRequest> requests = clientRequestRepository.findAll(spec);

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    private void updateClientName(List<ClientRequestDTO> reuests) {

        Map<String, String> individualNameMap = new HashMap<>();
        Map<String, String> individualIdentityNo = new HashMap<>();
        Map<String, IndividualIdentityType> individualIdentityType = new HashMap<>();
        Map<String, String> individualEmailAddress = new HashMap<>();

        Map<String, String> organisationNameMap = new HashMap<>();
        Map<String, String> organisationRegistration = new HashMap<>();
        Map<String, String> organisationEmailAddress = new HashMap<>();

        reuests.forEach(r -> {
            if (r.getTarget() == TargetEntity.INDIVIDUAL) {

                String name = individualNameMap.get(r.getTargetId());
                String identityNo = individualIdentityNo.get(r.getTargetId());
                IndividualIdentityType identityType = individualIdentityType.get(r.getTargetId());
                String emailAddress = individualEmailAddress.get(r.getTargetId());

                if(!individualNameMap.containsKey(r.getTargetId())) {

                    Individual individual = individualRepository.findById(UUID.fromString(r.getTargetId()))
                            .orElse(null);

                    if (individual != null) {
                        name = individual.getFirstName() + " " + individual.getSurname();
                        identityNo = individual.getIdentityNo();
                        identityType = individual.getIdentityType();
                        emailAddress = individual.getEmailAddress();
                    } else {
                        name = "Unknown Individual";
                        identityNo = "Unknown Identity No";
                        identityType = null; // Default fallback
                        emailAddress = "Unknown Email";
                    }

                    individualNameMap.put(r.getTargetId(), name);
                    individualIdentityNo.put(r.getTargetId(), identityNo);
                    individualIdentityType.put(r.getTargetId(), identityType);
                    individualEmailAddress.put(r.getTargetId(), emailAddress);
                }

                r.setName(name);
                r.setRegistration(identityNo);
                r.setIdentityType(identityType);
                r.setEmailAddress(emailAddress);

            } else if (r.getTarget() == TargetEntity.ORGANISATION) {
                
                String name = organisationNameMap.get(r.getTargetId());
                String registrationNo = organisationRegistration.get(r.getTargetId());
                String emailAddress = organisationEmailAddress.get(r.getTargetId());

                if(!organisationNameMap.containsKey(r.getTargetId())) {

                    Organisation organisation = organisationRepository.findById(UUID.fromString(r.getTargetId()))
                            .orElse(null);

                    if (organisation != null) {
                        name = organisation.getName();
                        registrationNo = organisation.getRegistrationNo();
                        emailAddress = organisation.getContactEmailAddress();
                    } else {
                        name = "Unknown Organisation";
                        registrationNo = "Unknown Registration No";
                        emailAddress = "Unknown Email";
                    }

                    organisationNameMap.put(r.getTargetId(), name);
                    organisationRegistration.put(r.getTargetId(), registrationNo);
                    organisationEmailAddress.put(r.getTargetId(), emailAddress);
                }

                r.setName(name);
                r.setRegistration(registrationNo);
                r.setEmailAddress(emailAddress);
            }
        });

    }

    /**
     * @see bw.co.knowvera.organisation.client.ClientRequestService#findByIndividual(String,
     *      Integer, Integer)
     */
    @Override
    protected Page<ClientRequestDTO> handleFindByIndividual(String individualId, Integer pageNumber, Integer pageSize)
            throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setTargetId(individualId);
        criteria.setTarget(TargetEntity.INDIVIDUAL);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));

        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);

        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    @Override
    protected List<ClientRequestDTO> handleFindByDocument(String documentId) throws Exception {

        return null; // clientRequestRepository.findByDocumentId(documentId);
    }

    @Override
    protected Page<ClientRequestDTO> handleFindByDocument(String documentId, Integer pageNumber, Integer pageSize)
            throws Exception {
        return null; // clientRequestRepository.findByDocumentId(documentId,
                     // PageRequest.of(pageNumber, pageSize));
    }

    @Override
    protected Page<ClientRequestDTO> handleFindByStatus(ClientRequestStatus status, Integer pageNumber,
            Integer pageSize) throws Exception {

        Specification<ClientRequest> spec = (root, query, cb) -> cb.equal(root.get("status"), status);

        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));
        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    @Override
    protected List<ClientRequestDTO> handleFindByTarget(TargetEntity target, String targetId) throws Exception {

        Specification<ClientRequest> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("target"), target),
                cb.equal(root.get("targetId"), targetId));

        List<ClientRequest> requests = clientRequestRepository.findAll(spec);
        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    @Override
    protected Page<ClientRequestDTO> handleFindByTarget(TargetEntity target, String targetId, Integer pageNumber,
            Integer pageSize) throws Exception {

        Specification<ClientRequest> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("target"), target),
                cb.equal(root.get("targetId"), targetId));

        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));
        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    @Override
    protected List<ClientRequestDTO> handleFindByTargetAndOrganisation(TargetEntity target, String targetId,
            String organisationId) throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setTarget(target);
        criteria.setTargetId(targetId);
        criteria.setOrganisationId(organisationId);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        List<ClientRequest> requests = clientRequestRepository.findAll(spec);
        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    @Override
    protected Page<ClientRequestDTO> handleFindByTargetAndOrganisation(TargetEntity target, String targetId,
            String organisationId, Integer pageNumber, Integer pageSize) throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setTarget(target);
        criteria.setTargetId(targetId);
        criteria.setOrganisationId(organisationId);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));
        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    @Override
    protected ClientRequestDTO handleUpdateStatus(String id, ClientRequestStatus status) throws Exception {

        ClientRequest clientRequest = clientRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ClientRequestServiceException("ClientRequest not found"));

        clientRequest.setStatus(status);
        clientRequest = clientRequestRepository.save(clientRequest);

        if (status == ClientRequestStatus.ACCEPTED) {
            // Additional actions on approval can be handled here
        }

        return clientRequestMapper.toClientRequestDTO(clientRequest);
    }

    @Override
    protected String handleConfirmToken(String requestId, String token) throws Exception {

        ClientRequest clientRequest = clientRequestRepository.findById(UUID.fromString(requestId))
                .orElseThrow(() -> new ClientRequestServiceException("ClientRequest not found"));

        boolean matches = passwordEncoder.matches(token, clientRequest.getAccountRequestToken());

        if (!matches || RequestTokens.isExpired(token, requestLinkTtl, clientRequest.getCreatedAt())) {
            throw new ClientRequestServiceException("Invalid or expired confirmation token");
        }

        String confirmationToken = RequestTokens.issue(requestTokenLength);

        // Encode the token
        String encodedToken = passwordEncoder.encode(confirmationToken);

        clientRequest.setIdentityConfirmationToken(encodedToken);

        String registrationToken = RequestTokens.issue(requestTokenLength);

        // Encode the token
        String encodedRegistrationToken = passwordEncoder.encode(registrationToken);

        clientRequest.setRegistrationToken(encodedRegistrationToken);

        clientRequestRepository.save(clientRequest);

        return String.format("%s|%s", confirmationToken, registrationToken);
    }

    @Override
    protected Long handleCountByStatus(ClientRequestStatus status) throws Exception {

        return clientRequestRepository.countByStatus(status).orElse(0L);
    }

    @Override
    protected Long handleCountByStatusAndOrganisationId(ClientRequestStatus status, String organisationId)
            throws Exception {

        return clientRequestRepository.countByStatusAndOrganisationId(status, organisationId).orElse(0L);
    }

    @Override
    protected Long handleCount() throws Exception {

        return clientRequestRepository.count();
    }

    @Override
    protected boolean handleConfirmRegistration(String id, Boolean confirm, String registrationToken) throws Exception {

        ClientRequest request = clientRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ClientRequestServiceException("ClientRequest not found"));

        boolean matches = request.getRegistrationToken() != null
                && passwordEncoder.matches(registrationToken, request.getRegistrationToken());

        if (!matches || RequestTokens.isExpired(registrationToken, requestSessionTtl, request.getModifiedAt())) {
            throw new ClientRequestServiceException("Invalid or expired registration token");
        }

        if (request.getStatus() == ClientRequestStatus.ACCEPTED || request.getStatus() == ClientRequestStatus.REJECTED) {

            throw new ClientRequestServiceException("ClientRequest already decided");
        }

        // Single use: the decision consumes the registration token
        request.setRegistrationToken(null);

        if (confirm) {
            request.setStatus(ClientRequestStatus.ACCEPTED);
        } else {
            request.setStatus(ClientRequestStatus.REJECTED);
        }

        request = clientRequestRepository.save(request);

        return request.getStatus() == ClientRequestStatus.ACCEPTED;
    }

    @Override
    protected ClientRequestDTO handleFindUserReadyRequests() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleFindUserReadyRequests'");
    }

    @Override
    protected ClientRequestDTO handleFindUserReadyRequests(Integer pageNumber, Integer pageSize) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleFindUserReadyRequests'");
    }

}