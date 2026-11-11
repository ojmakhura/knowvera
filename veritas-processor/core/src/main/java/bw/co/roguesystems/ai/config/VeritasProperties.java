package bw.co.roguesystems.ai.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

/**
 * Settings of the processor (prefix {@code veritas}).
 */
@ConfigurationProperties(prefix = "veritas")
public class VeritasProperties {

    /** Never use providers that send the document out of the deployment (e.g. Gemini). */
    private boolean localOnly = false;

    private final Storage storage = new Storage();
    private final Tesseract tesseract = new Tesseract();
    private final LlmServer lmStudio = new LlmServer("http://localhost:1234", "gemma4");
    private final LlmServer ollama = new LlmServer("http://localhost:11434", "gemma4");
    private final Bootstrap bootstrap = new Bootstrap();
    private final Jobs jobs = new Jobs();
    private final Limits limits = new Limits();

    public boolean isLocalOnly() {
        return localOnly;
    }

    public void setLocalOnly(boolean localOnly) {
        this.localOnly = localOnly;
    }

    public Storage getStorage() {
        return storage;
    }

    public Tesseract getTesseract() {
        return tesseract;
    }

    public LlmServer getLmStudio() {
        return lmStudio;
    }

    public LlmServer getOllama() {
        return ollama;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public Jobs getJobs() {
        return jobs;
    }

    public Limits getLimits() {
        return limits;
    }

    public static class Jobs {

        /** Jobs processed at once, as min-max consumers of the job queue. */
        private String concurrency = "1-4";

        /** Times a step is tried when its providers were unreachable, timed out or failed themselves. */
        private int attempts = 3;

        /** Wait before a step's second try; doubles with every further one. */
        private Duration retryDelay = Duration.ofSeconds(5);

        /** A job still running after this long was interrupted: it is failed and its caller told. */
        private Duration staleAfter = Duration.ofHours(2);

        /** How long the job history (text and fields included) is kept; null keeps it forever. */
        private Duration retention;

        public String getConcurrency() {
            return concurrency;
        }

        public void setConcurrency(String concurrency) {
            this.concurrency = concurrency;
        }

        public int getAttempts() {
            return attempts;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }

        public Duration getRetryDelay() {
            return retryDelay;
        }

        public void setRetryDelay(Duration retryDelay) {
            this.retryDelay = retryDelay;
        }

        public Duration getStaleAfter() {
            return staleAfter;
        }

        public void setStaleAfter(Duration staleAfter) {
            this.staleAfter = staleAfter;
        }

        public Duration getRetention() {
            return retention;
        }

        public void setRetention(Duration retention) {
            this.retention = retention;
        }
    }

    public static class Limits {

        /** Largest document read from storage. */
        private DataSize maxFileSize = DataSize.ofMegabytes(25);

        /** Most pages of a PDF that are read or rendered for OCR. */
        private int maxPages = 50;

        /**
         * Fewest characters per page for a PDF's text layer to count as the document's text: a
         * scanned PDF often carries a stamp or header as text, and still needs OCR.
         */
        private int minTextCharsPerPage = 50;

        public DataSize getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(DataSize maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public int getMaxPages() {
            return maxPages;
        }

        public void setMaxPages(int maxPages) {
            this.maxPages = maxPages;
        }

        public int getMinTextCharsPerPage() {
            return minTextCharsPerPage;
        }

        public void setMinTextCharsPerPage(int minTextCharsPerPage) {
            this.minTextCharsPerPage = minTextCharsPerPage;
        }
    }

    public static class Storage {

        /** The bucket the callers upload documents to. */
        private String bucket = "knowvera";

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }
    }

    public static class Tesseract {

        /** Directory holding the traineddata files. */
        private String datapath = "/usr/share/tesseract-ocr/5/tessdata";

        /** Default languages, e.g. "eng" or "eng+tsn" (a step's ocrLanguages option overrides it). */
        private String languages = "eng";

        /** Resolution pages are rendered at (a step's ocrDpi option overrides it). */
        private int dpi = 300;

        public String getDatapath() {
            return datapath;
        }

        public void setDatapath(String datapath) {
            this.datapath = datapath;
        }

        public String getLanguages() {
            return languages;
        }

        public void setLanguages(String languages) {
            this.languages = languages;
        }

        public int getDpi() {
            return dpi;
        }

        public void setDpi(int dpi) {
            this.dpi = dpi;
        }
    }

    public static class LlmServer {

        private String baseUrl;

        /** Model used when a step names none. */
        private String model;

        /** How long to wait for the connection. */
        private Duration connectTimeout = Duration.ofSeconds(10);

        /** How long to wait for the model's answer; after it the step tries its next provider. */
        private Duration readTimeout = Duration.ofMinutes(5);

        /** Ollama's context window in tokens (a step's numCtx option overrides it); LM Studio sets its own. */
        private int numCtx = 8192;

        public LlmServer(String baseUrl, String model) {
            this.baseUrl = baseUrl;
            this.model = model;
        }

        public int getNumCtx() {
            return numCtx;
        }

        public void setNumCtx(int numCtx) {
            this.numCtx = numCtx;
        }

        public Duration getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Duration getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }

    public static class Bootstrap {

        /** Create the "default" profile at startup when it does not exist. */
        private boolean defaultProfile = true;

        public boolean isDefaultProfile() {
            return defaultProfile;
        }

        public void setDefaultProfile(boolean defaultProfile) {
            this.defaultProfile = defaultProfile;
        }
    }
}
