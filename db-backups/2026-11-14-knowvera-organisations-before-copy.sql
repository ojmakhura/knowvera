--
-- PostgreSQL database dump
--

\restrict ltOVbQv4pm8uziklcUEcLxW2OLj6ooOGznJ0zy0wbukl4Vrkoco0nvEDtHdOsyV

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

--
-- Data for Name: organisation; Type: TABLE DATA; Schema: public; Owner: rogueadmin
--

COPY public.organisation (id, created_at, created_by, modified_at, modified_by, closed_at, code, contact_email_address, country_of_registration, description, domains, is_client, keycloak_id, kyc_status, legal_form, name, nature_of_business, phone_numbers, physical_address, postal_address, registration_date, registration_no, retain_until, status, tax_number, trading_name, vat_number) FROM stdin;
01a1db66-df4b-717f-a0d7-3636d2b5e20f	2026-11-14 20:51:54.566047	developer	2026-11-14 21:37:52.568082	developer	\N	QAH	qa.holdings@example.com	Botswana	\N	[]	t	99feff72-57b4-4ae2-9dbb-491d5556395f	ABSENT	PRIVATE_COMPANY	QA Holdings (Pty) Ltd	\N	[]	Plot 1234, Main Mall, Gaborone	P O Box 1234, Gaborone	\N	BW00001234567	\N	ACTIVE	\N	\N	\N
\.


--
-- Data for Name: individual_fields2organisation; Type: TABLE DATA; Schema: public; Owner: rogueadmin
--

COPY public.individual_fields2organisation (organisation_fk, individual_fields_fk) FROM stdin;
01a1db66-df4b-717f-a0d7-3636d2b5e20f	36775102-e27c-4eab-a1b7-4780b9d2ef7d
01a1db66-df4b-717f-a0d7-3636d2b5e20f	0cd6c563-6470-44ec-9dc3-c083b7aea44f
01a1db66-df4b-717f-a0d7-3636d2b5e20f	4869751f-bfc8-450c-8b71-d816cb0ea2e1
01a1db66-df4b-717f-a0d7-3636d2b5e20f	8691f90b-819a-4ce7-8970-2ee35e040ef7
01a1db66-df4b-717f-a0d7-3636d2b5e20f	822ce603-43f2-4ad6-92b7-2178404bde39
01a1db66-df4b-717f-a0d7-3636d2b5e20f	b078fc6e-6450-4053-a961-b27c4da9d72c
01a1db66-df4b-717f-a0d7-3636d2b5e20f	a35f4965-a6b6-4c6b-b4dc-43d31f50760f
01a1db66-df4b-717f-a0d7-3636d2b5e20f	148b3ab9-0708-4107-b2c5-820cc40977da
01a1db66-df4b-717f-a0d7-3636d2b5e20f	802ae801-2da9-4a89-89ac-36236cfe82b2
01a1db66-df4b-717f-a0d7-3636d2b5e20f	433c7652-6c25-4f2b-b36b-57b1ced7c4ef
01a1db66-df4b-717f-a0d7-3636d2b5e20f	de16a289-806e-498b-8e73-3edbaa786de6
01a1db66-df4b-717f-a0d7-3636d2b5e20f	0899cfbe-20c8-4519-a3cd-2396ccd262f5
01a1db66-df4b-717f-a0d7-3636d2b5e20f	d1ca2f90-9ced-432e-9437-1e5f74352a73
01a1db66-df4b-717f-a0d7-3636d2b5e20f	f2e770dd-3a21-4b5f-8366-cc8c1976b1db
\.


--
-- Data for Name: organisation2organisation_fields; Type: TABLE DATA; Schema: public; Owner: rogueadmin
--

COPY public.organisation2organisation_fields (organisation_fk, organisation_fields_fk) FROM stdin;
01a1db66-df4b-717f-a0d7-3636d2b5e20f	f34b5866-b8f2-41f3-a095-2281db200b7d
01a1db66-df4b-717f-a0d7-3636d2b5e20f	50eed5df-025c-49bd-9f0e-3b85af3e9452
01a1db66-df4b-717f-a0d7-3636d2b5e20f	e74764f0-5b46-4db1-b59a-eb7650836a11
01a1db66-df4b-717f-a0d7-3636d2b5e20f	3efb4ae3-b37d-4969-9d6e-b1cc513aeebe
01a1db66-df4b-717f-a0d7-3636d2b5e20f	7bf44610-f4b3-4455-afc9-269694c7154d
01a1db66-df4b-717f-a0d7-3636d2b5e20f	05882a20-e6cf-4b4b-8858-226d3eca32aa
01a1db66-df4b-717f-a0d7-3636d2b5e20f	f854b6a8-0974-449b-ba34-bbe990a18f53
01a1db66-df4b-717f-a0d7-3636d2b5e20f	bb07c222-2bb5-4f70-95d4-94a2a173cb15
\.


--
-- PostgreSQL database dump complete
--

\unrestrict ltOVbQv4pm8uziklcUEcLxW2OLj6ooOGznJ0zy0wbukl4Vrkoco0nvEDtHdOsyV

