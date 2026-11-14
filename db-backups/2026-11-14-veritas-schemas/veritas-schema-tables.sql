--
-- PostgreSQL database dump
--

\restrict hg5L0BTI28IyslZdTgjiF4nMH7bVJ8wU3QC9TsyxI7w272BievRBxviL3oc8dNg

-- Dumped from database version 18.6 (Debian 18.6-1.pgdg13+2)
-- Dumped by pg_dump version 18.6 (Debian 18.6-1.pgdg13+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: veritas_schema; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.veritas_schema (
    id uuid NOT NULL,
    code character varying(100) NOT NULL,
    version integer NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    created_at timestamp with time zone NOT NULL,
    created_by character varying(255)
);


--
-- Name: veritas_schema_field; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.veritas_schema_field (
    schema_id uuid NOT NULL,
    "position" integer NOT NULL,
    name character varying(255) NOT NULL,
    label character varying(255),
    field_type character varying(20) NOT NULL,
    format character varying(255),
    many boolean NOT NULL,
    mandatory boolean NOT NULL
);


--
-- Data for Name: veritas_schema; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.veritas_schema (id, code, version, name, description, created_at, created_by) FROM stdin;
78136a17-2774-4f70-aa3e-37fc107f4515	AFD	1	Affidavit	\N	2026-11-11 22:19:12.737785+02	legacy-migration
b5ba52aa-9d6a-4711-8685-756fc5c0f784	BNCN	1	Bank Account Confirmation Letter	\N	2026-11-11 22:19:12.737785+02	legacy-migration
72cdc98d-7259-4be1-846f-f34aa022b2b5	BNKS	1	Bank Statement	\N	2026-11-11 22:19:12.737785+02	legacy-migration
96ec88e2-0d90-47fb-b7b6-899b20f9867a	CEXT	1	Company Extract	\N	2026-11-11 22:19:12.737785+02	legacy-migration
6be2ddd8-78c8-4902-ab4c-c8d3bc57b7fc	CIPA	1	CIPA Certificate	\N	2026-11-11 22:19:12.737785+02	legacy-migration
7eae0b92-645d-46b9-bc6a-e73ec5d65d8f	CLRF	1	Client Request File	\N	2026-11-11 22:19:12.737785+02	legacy-migration
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	EMPL	1	Employment Letter	\N	2026-11-11 22:19:12.737785+02	legacy-migration
e306a1c9-7ca7-43c8-b6ae-d243c29267c9	INV	1	Invoice	\N	2026-11-11 22:19:12.737785+02	legacy-migration
f45c9fae-db35-489e-9d60-2d5c45b27468	INVT	1	Invoice Template	\N	2026-11-11 22:19:12.737785+02	legacy-migration
6d8e5d30-2484-478f-9bd7-d6330a8f2dde	LAL	1	Local Authority Letter	\N	2026-11-11 22:19:12.737785+02	legacy-migration
b2cc2793-2a3c-400a-aed2-427ac1e58ab8	LSA	1	Lease Agreement	\N	2026-11-11 22:19:12.737785+02	legacy-migration
39e86928-4ef5-4c6e-9521-e3a281464d45	NID	1	National Identity Card	\N	2026-11-11 22:19:12.737785+02	legacy-migration
9cb44a43-cf8e-4086-a0c5-937d11270353	OTHER	1	Other	\N	2026-11-11 22:19:12.737785+02	legacy-migration
89197760-6968-4fe3-ad27-c5d249da4ed5	PFI	1	Proof of Income	\N	2026-11-11 22:19:12.737785+02	legacy-migration
a6894aa9-61c5-4d06-a0cb-21ed8cd7d2a3	PRAD	1	Proof of Residence	\N	2026-11-11 22:19:12.737785+02	legacy-migration
1b868787-802e-41d9-8288-086c0b4d0cd3	PR_DEL_NT	1	Prax Botswana Delivery Note	\N	2026-11-11 22:19:12.737785+02	legacy-migration
0b73af22-6f3a-435e-800c-4bd75339bd1a	PSPT	1	Passport	\N	2026-11-11 22:19:12.737785+02	legacy-migration
32e705d0-4065-453a-b0c0-0edd970f37fb	QOT	1	Quotation	\N	2026-11-11 22:19:12.737785+02	legacy-migration
583962b1-fb3d-4e6d-9bc7-c05bb8e2301f	QOTT	1	Quotation Template	\N	2026-11-11 22:19:12.737785+02	legacy-migration
d77615a5-8131-4193-bca6-ee1978f4f544	RSP	1	Residence Permit	\N	2026-11-11 22:19:12.737785+02	legacy-migration
d09dad4d-c7e4-4b63-ad96-42cfd59a9477	SHCT	1	Share Certificate	\N	2026-11-11 22:19:12.737785+02	legacy-migration
403ff812-e30e-4b9e-bbc9-fe2a3227caaf	TSTS	1	Testes	\N	2026-11-11 22:19:12.737785+02	legacy-migration
d25fd7ce-ebee-416c-844e-afdc46bffea1	UTL	1	Utility Bill	\N	2026-11-11 22:19:12.737785+02	legacy-migration
\.


--
-- Data for Name: veritas_schema_field; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.veritas_schema_field (schema_id, "position", name, label, field_type, format, many, mandatory) FROM stdin;
b5ba52aa-9d6a-4711-8685-756fc5c0f784	0	firstName	First Name	STRING	\N	f	t
b5ba52aa-9d6a-4711-8685-756fc5c0f784	1	middleNames	Middle Name	STRING	\N	f	f
b5ba52aa-9d6a-4711-8685-756fc5c0f784	2	documentDate	Document Date	DATE	\N	f	f
b5ba52aa-9d6a-4711-8685-756fc5c0f784	3	accountOpeningDate	Account Opening Date	DATE	\N	f	f
b5ba52aa-9d6a-4711-8685-756fc5c0f784	4	accountName	Account Name	STRING	\N	f	f
b5ba52aa-9d6a-4711-8685-756fc5c0f784	5	accountNumber	Account Number	STRING	\N	f	t
b5ba52aa-9d6a-4711-8685-756fc5c0f784	6	branchCode	Branch Code	STRING	\N	f	f
b5ba52aa-9d6a-4711-8685-756fc5c0f784	7	branchName	Branch Name	STRING	\N	f	f
b5ba52aa-9d6a-4711-8685-756fc5c0f784	8	bankName	Bank Name	STRING	\N	f	t
b5ba52aa-9d6a-4711-8685-756fc5c0f784	9	swiftCode	SWIFT Code	STRING	\N	f	f
b5ba52aa-9d6a-4711-8685-756fc5c0f784	10	surname	Surname	STRING	\N	f	t
b5ba52aa-9d6a-4711-8685-756fc5c0f784	11	organisationAccountNumber	Account Number	STRING	\N	f	f
b5ba52aa-9d6a-4711-8685-756fc5c0f784	12	organisationName	Organisation Account Name	STRING	\N	f	t
96ec88e2-0d90-47fb-b7b6-899b20f9867a	0	uin	UIN	STRING	\N	f	t
96ec88e2-0d90-47fb-b7b6-899b20f9867a	1	companyName	Company Name	STRING	\N	f	t
96ec88e2-0d90-47fb-b7b6-899b20f9867a	2	companyType	Company Type	STRING	\N	f	f
96ec88e2-0d90-47fb-b7b6-899b20f9867a	3	exempt	Exempt	STRING	Yes or No	f	f
96ec88e2-0d90-47fb-b7b6-899b20f9867a	4	companyStatus	Company Status	STRING	\N	f	f
96ec88e2-0d90-47fb-b7b6-899b20f9867a	5	incorporationDate	Incorporation Date	DATE	\N	f	t
96ec88e2-0d90-47fb-b7b6-899b20f9867a	6	hasConstitution	Have own constitution?	STRING	\N	f	f
96ec88e2-0d90-47fb-b7b6-899b20f9867a	7	physicalAddress	Principal Place of Business	STRING	\N	f	t
96ec88e2-0d90-47fb-b7b6-899b20f9867a	8	postalAddress	Postal Address	STRING	\N	f	t
96ec88e2-0d90-47fb-b7b6-899b20f9867a	9	directors	Directors	OBJECT	[{\n  name: string,\n  residentialAddress: string,\n  postalAddress: string,\n  appointmentDate: date\n}]	t	f
96ec88e2-0d90-47fb-b7b6-899b20f9867a	10	secretaries	Secretaries	STRING	[{\n  name: string,\n  uin: string,\n  registeredOfficeAddress: string,\n  appointmentDate: date\n}]	t	f
96ec88e2-0d90-47fb-b7b6-899b20f9867a	11	totalShares	Total Shares	NUMBER	\N	f	f
96ec88e2-0d90-47fb-b7b6-899b20f9867a	12	shareholders	Shareholders	OBJECT	[{\n  name: string,\n  residentialAddress: string,\n  postalAddress: string,\n  appointmentDate: date,\n  numberOfShares: number\n}]	t	f
96ec88e2-0d90-47fb-b7b6-899b20f9867a	13	documentDate	Document Date	DATE	\N	f	f
6be2ddd8-78c8-4902-ab4c-c8d3bc57b7fc	0	uin	UIN	STRING	\N	f	t
6be2ddd8-78c8-4902-ab4c-c8d3bc57b7fc	1	companyName	Company Name	STRING	\N	f	t
6be2ddd8-78c8-4902-ab4c-c8d3bc57b7fc	2	companyType	Company Type	STRING	\N	f	f
6be2ddd8-78c8-4902-ab4c-c8d3bc57b7fc	3	incorporationDate	Incorporation Date	STRING	\N	f	t
6be2ddd8-78c8-4902-ab4c-c8d3bc57b7fc	4	placeOfBusiness	Principal Place of Business	STRING	\N	f	t
6be2ddd8-78c8-4902-ab4c-c8d3bc57b7fc	5	postalAddress	Postal Address	STRING	\N	f	t
6be2ddd8-78c8-4902-ab4c-c8d3bc57b7fc	6	directors	Directors	OBJECT	[{\n   name: string,\n   residentialAddress: string,\n   postalAddress: string,\n   appointmentDate: date\n}]	t	f
6be2ddd8-78c8-4902-ab4c-c8d3bc57b7fc	7	registrationDate	Registration Date	DATE	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	0	individualHonorificTitle	Honorific Title	STRING	\N	f	f
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	1	individualFirstName	First Name	STRING	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	2	individualMiddleNames	Middle Name(s)	STRING	\N	f	f
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	3	individualSurname	Surname	STRING	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	4	individualIdentityNo	Identity Number	STRING	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	5	individualPostalAddress	Individual Postal Address	STRING	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	6	individualPhysicalAddress	Individual Physical Address	STRING	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	7	employmentPosition	Position	STRING	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	8	organisationNameAbbreviation	Abbreviation	STRING	\N	f	f
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	9	organisationName	Company Name	STRING	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	10	organisationContactPerson	Contact Person	STRING	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	11	organisationPhoneNumber	Company Phone Number(s)	STRING	\N	f	f
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	12	organisationPostalAddress	Company Postal Address	STRING	\N	f	f
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	13	organisationPhysicalAddress	Company Physical Address	STRING	\N	f	t
d03d3bb6-7d3a-4c92-9b75-6e48bce69c88	14	documentDate	Document Date	DATE	\N	f	t
39e86928-4ef5-4c6e-9521-e3a281464d45	0	firstName	First Name	STRING	\N	f	t
39e86928-4ef5-4c6e-9521-e3a281464d45	1	middleName	Middle Name	STRING	\N	f	f
39e86928-4ef5-4c6e-9521-e3a281464d45	2	surname	Surname	STRING	\N	f	t
39e86928-4ef5-4c6e-9521-e3a281464d45	3	dateOfBirth	Date of Birth	DATE	\N	f	t
39e86928-4ef5-4c6e-9521-e3a281464d45	4	identityNo	ID Number	STRING	^\\d{4}[12]\\d{4}$	f	t
39e86928-4ef5-4c6e-9521-e3a281464d45	5	placeOfBirth	Place of Birth	STRING	\N	f	f
39e86928-4ef5-4c6e-9521-e3a281464d45	6	nationality	Nationality	STRING	\N	f	f
39e86928-4ef5-4c6e-9521-e3a281464d45	7	sex	Sex	STRING	If it is M, set it for MALE\nif it is F, set it for FEMALE\nOtherwise set it to UNSPECIFIED	f	f
39e86928-4ef5-4c6e-9521-e3a281464d45	8	expiryDate	Date of Expiry	DATE	The date is in the form dd/MM/yyyy, you should convert it to mm/DD/yyyy	f	f
39e86928-4ef5-4c6e-9521-e3a281464d45	9	certifiedOn	Certified On	DATE	dd MMM yyyy, convert it to MM/dd/yyyy	f	f
39e86928-4ef5-4c6e-9521-e3a281464d45	10	certifiedBy	Certified By	STRING	\N	f	f
1b868787-802e-41d9-8288-086c0b4d0cd3	0	deliveryBarcode	File Folder	STRING	May be called referenceNo	f	t
1b868787-802e-41d9-8288-086c0b4d0cd3	1	deliveryNoteNo	Delivery Note	STRING	\N	f	t
1b868787-802e-41d9-8288-086c0b4d0cd3	2	soldToCustomerNo	Sold To (Customer Number)	STRING	\N	f	t
1b868787-802e-41d9-8288-086c0b4d0cd3	3	soldToOrganisation	Sold To (Organisation Name)	STRING	\N	f	t
1b868787-802e-41d9-8288-086c0b4d0cd3	4	deliverdToCustomerNo	Delivered To (Customer No.)	STRING	\N	f	f
1b868787-802e-41d9-8288-086c0b4d0cd3	5	deliveredToCustomer	Delivered To (Customer Name)	STRING	\N	f	t
1b868787-802e-41d9-8288-086c0b4d0cd3	6	salesOrderNo	Sales Order	STRING	\N	f	t
1b868787-802e-41d9-8288-086c0b4d0cd3	7	salesOrderDate	Date	DATE	\N	f	t
403ff812-e30e-4b9e-bbc9-fe2a3227caaf	0	tst2	Test 2	STRING	gdfgd	f	t
\.


--
-- Name: veritas_schema uk_veritas_schema_code_version; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.veritas_schema
    ADD CONSTRAINT uk_veritas_schema_code_version UNIQUE (code, version);


--
-- Name: veritas_schema_field veritas_schema_field_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.veritas_schema_field
    ADD CONSTRAINT veritas_schema_field_pkey PRIMARY KEY (schema_id, "position");


--
-- Name: veritas_schema veritas_schema_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.veritas_schema
    ADD CONSTRAINT veritas_schema_pkey PRIMARY KEY (id);


--
-- Name: veritas_schema_field fk_veritas_field_schema; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.veritas_schema_field
    ADD CONSTRAINT fk_veritas_field_schema FOREIGN KEY (schema_id) REFERENCES public.veritas_schema(id);


--
-- PostgreSQL database dump complete
--

\unrestrict hg5L0BTI28IyslZdTgjiF4nMH7bVJ8wU3QC9TsyxI7w272BievRBxviL3oc8dNg

