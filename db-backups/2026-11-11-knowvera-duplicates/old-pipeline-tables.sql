--
-- PostgreSQL database dump
--

\restrict OgDV6in3AyEmRZuoFVsohI7i8GcWiDx4MUaukZdlrFK0jSmlshEjtfUS9s2B4x0

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
-- Name: document_confirmation_tools2settings; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.document_confirmation_tools2settings (
    settings_fk uuid NOT NULL,
    document_confirmation_tools_fk uuid CONSTRAINT document_confirmation_tools_document_confirmation_tool_not_null NOT NULL
);


--
-- Name: settings2text_cleanup_tools; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.settings2text_cleanup_tools (
    settings_fk uuid NOT NULL,
    text_cleanup_tools_fk uuid NOT NULL
);


--
-- Name: settings2text_extraction_tools; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.settings2text_extraction_tools (
    settings_fk uuid NOT NULL,
    text_extraction_tools_fk uuid CONSTRAINT settings2text_extraction_tool_text_extraction_tools_fk_not_null NOT NULL
);


--
-- Name: settings2text_processing_tools; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.settings2text_processing_tools (
    settings_fk uuid NOT NULL,
    text_processing_tools_fk uuid CONSTRAINT settings2text_processing_tool_text_processing_tools_fk_not_null NOT NULL
);


--
-- Name: tool_selector; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tool_selector (
    id uuid NOT NULL,
    "position" integer,
    tool character varying(255) NOT NULL,
    CONSTRAINT tool_selector_tool_check CHECK (((tool)::text = ANY (ARRAY[('TESSERACT'::character varying)::text, ('OLLAMA'::character varying)::text, ('LM_STUDIO'::character varying)::text, ('GEMINI'::character varying)::text])))
);


--
-- Data for Name: document_confirmation_tools2settings; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.document_confirmation_tools2settings (settings_fk, document_confirmation_tools_fk) FROM stdin;
52155fa4-14c3-4bad-a1c2-0e321a022346	98de049b-17f3-4251-943a-54e184230909
52155fa4-14c3-4bad-a1c2-0e321a022346	efb03d06-fd77-4b1d-bd04-b3fe91017c3e
\.


--
-- Data for Name: settings2text_cleanup_tools; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.settings2text_cleanup_tools (settings_fk, text_cleanup_tools_fk) FROM stdin;
52155fa4-14c3-4bad-a1c2-0e321a022346	79f5d782-cf87-446a-83f3-12bcc8f5d7d0
52155fa4-14c3-4bad-a1c2-0e321a022346	3a9204f1-335e-401a-84a6-454a87a274f0
\.


--
-- Data for Name: settings2text_extraction_tools; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.settings2text_extraction_tools (settings_fk, text_extraction_tools_fk) FROM stdin;
52155fa4-14c3-4bad-a1c2-0e321a022346	93f65779-08b7-4a65-9586-777c5e4cacc8
52155fa4-14c3-4bad-a1c2-0e321a022346	88a4c0ac-767b-43e4-b9dd-50587eb524aa
\.


--
-- Data for Name: settings2text_processing_tools; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.settings2text_processing_tools (settings_fk, text_processing_tools_fk) FROM stdin;
52155fa4-14c3-4bad-a1c2-0e321a022346	c14a297a-7628-41d8-989b-44d1ad932211
52155fa4-14c3-4bad-a1c2-0e321a022346	302204cb-ef92-42d5-847d-514a6d6e1158
\.


--
-- Data for Name: tool_selector; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tool_selector (id, "position", tool) FROM stdin;
efb03d06-fd77-4b1d-bd04-b3fe91017c3e	2	GEMINI
98de049b-17f3-4251-943a-54e184230909	1	OLLAMA
302204cb-ef92-42d5-847d-514a6d6e1158	2	GEMINI
c14a297a-7628-41d8-989b-44d1ad932211	1	OLLAMA
3a9204f1-335e-401a-84a6-454a87a274f0	2	GEMINI
79f5d782-cf87-446a-83f3-12bcc8f5d7d0	1	OLLAMA
5ac4d33f-c596-412c-b396-240e6f373128	2	OLLAMA
88a4c0ac-767b-43e4-b9dd-50587eb524aa	1	TESSERACT
93f65779-08b7-4a65-9586-777c5e4cacc8	0	GEMINI
\.


--
-- Name: tool_selector tool_selector_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tool_selector
    ADD CONSTRAINT tool_selector_pkey PRIMARY KEY (id);


--
-- Name: settings2text_cleanup_tools uk4y5ymovs2pblu8c1wwmdeppe9; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings2text_cleanup_tools
    ADD CONSTRAINT uk4y5ymovs2pblu8c1wwmdeppe9 UNIQUE (text_cleanup_tools_fk);


--
-- Name: document_confirmation_tools2settings uk8c28l4u5rg0l0wwr1nclxj1li; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.document_confirmation_tools2settings
    ADD CONSTRAINT uk8c28l4u5rg0l0wwr1nclxj1li UNIQUE (document_confirmation_tools_fk);


--
-- Name: settings2text_extraction_tools ukb56cuxtam35bkd8xbfdxdjifq; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings2text_extraction_tools
    ADD CONSTRAINT ukb56cuxtam35bkd8xbfdxdjifq UNIQUE (text_extraction_tools_fk);


--
-- Name: settings2text_processing_tools uktf8tktkp9ki4hfpbui9fib3pe; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings2text_processing_tools
    ADD CONSTRAINT uktf8tktkp9ki4hfpbui9fib3pe UNIQUE (text_processing_tools_fk);


--
-- Name: settings2text_cleanup_tools fk1qvvrg2iul8hq2tshff84ch29; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings2text_cleanup_tools
    ADD CONSTRAINT fk1qvvrg2iul8hq2tshff84ch29 FOREIGN KEY (settings_fk) REFERENCES public.settings(id);


--
-- Name: document_confirmation_tools2settings fk1s8rrwuor8ks8cf8q2k5c5hth; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.document_confirmation_tools2settings
    ADD CONSTRAINT fk1s8rrwuor8ks8cf8q2k5c5hth FOREIGN KEY (settings_fk) REFERENCES public.settings(id);


--
-- Name: settings2text_extraction_tools fk3tfuxbe2mtw3h0wxqpqbvsdwc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings2text_extraction_tools
    ADD CONSTRAINT fk3tfuxbe2mtw3h0wxqpqbvsdwc FOREIGN KEY (text_extraction_tools_fk) REFERENCES public.tool_selector(id);


--
-- Name: settings2text_processing_tools fk554ev6yctdtigkbrupix1e9va; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings2text_processing_tools
    ADD CONSTRAINT fk554ev6yctdtigkbrupix1e9va FOREIGN KEY (text_processing_tools_fk) REFERENCES public.tool_selector(id);


--
-- Name: settings2text_processing_tools fkcnlwrtbf3uighpcwk21ffkbr8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings2text_processing_tools
    ADD CONSTRAINT fkcnlwrtbf3uighpcwk21ffkbr8 FOREIGN KEY (settings_fk) REFERENCES public.settings(id);


--
-- Name: document_confirmation_tools2settings fkkkx5f7sjvitik0ughdk458nu6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.document_confirmation_tools2settings
    ADD CONSTRAINT fkkkx5f7sjvitik0ughdk458nu6 FOREIGN KEY (document_confirmation_tools_fk) REFERENCES public.tool_selector(id);


--
-- Name: settings2text_cleanup_tools fkoai56d85xmyg5g8s3ral6psyj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings2text_cleanup_tools
    ADD CONSTRAINT fkoai56d85xmyg5g8s3ral6psyj FOREIGN KEY (text_cleanup_tools_fk) REFERENCES public.tool_selector(id);


--
-- Name: settings2text_extraction_tools fkpgn5ajm6njmjoa8ub9eltv4oo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings2text_extraction_tools
    ADD CONSTRAINT fkpgn5ajm6njmjoa8ub9eltv4oo FOREIGN KEY (settings_fk) REFERENCES public.settings(id);


--
-- PostgreSQL database dump complete
--

\unrestrict OgDV6in3AyEmRZuoFVsohI7i8GcWiDx4MUaukZdlrFK0jSmlshEjtfUS9s2B4x0

