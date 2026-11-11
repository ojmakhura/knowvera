# Document storage (S3: Garage or SeaweedFS)

Uploaded documents are objects in one bucket on an S3-compatible server, run outside this
application (Garage, SeaweedFS, or AWS S3 itself). This replaces MinIO.

```
DocumentApiImpl / DocumentQueueProcessor / GeminiTextExtractionProcessor
 └ ObjectStorageService (core/.../storage)          put, get, delete, list one "directory", create bucket
    └ S3Client (AWS SDK v2), built by Spring Cloud AWS from spring.cloud.aws.*
       └ ObjectStorageConfig: S3ClientCustomizer (checksums, below)
          └ https://s3.example.com/<bucket>/<key>     path-style requests
```

Only basic S3 is used: put (multipart is not needed: uploads are at most 20 MB), get, delete,
ListObjectsV2 with a `/` delimiter, HeadBucket and CreateBucket. No bucket policies, versioning or
presigned URLs, so both Garage and SeaweedFS support everything the API does.

## Configuration

`webservice/src/main/resources/application.yml`:

```yaml
spring:
  cloud:
    aws:
      credentials:
        access-key: ${S3_ACCESS_KEY:${MINIO_ACCESS_KEY:}}
        secret-key: ${S3_SECRET_KEY:${MINIO_SECRET_KEY:}}
      region:
        static: ${S3_REGION:garage}
      s3:
        path-style-access-enabled: true

storage:
  s3:
    endpoint: ${S3_ENDPOINT:${MINIO_URL:}}
    bucket: ${S3_BUCKET:${MINIO_BUCKET:knowvera}}
    create-bucket: ${S3_CREATE_BUCKET:true}
```

| Variable | Default | |
|---|---|---|
| `S3_ENDPOINT` | `MINIO_URL` | The server's S3 API, e.g. `https://s3.example.com`. Blank: AWS S3 itself |
| `S3_REGION` | `garage` | Garage: its `s3_region` (default `garage`). SeaweedFS accepts any, e.g. `us-east-1` |
| `S3_ACCESS_KEY`, `S3_SECRET_KEY` | `MINIO_ACCESS_KEY`, `MINIO_SECRET_KEY` | The key the server issued for the API |
| `S3_BUCKET` | `MINIO_BUCKET`, then `knowvera` | |
| `S3_CREATE_BUCKET` | `true` | Create the bucket on the first upload; turn off when the key may not create buckets |

Until the `S3_*` variables are set, the `MINIO_*` ones apply, i.e. the existing MinIO keeps
working. Switch by setting the `S3_*` variables and restarting the API.

The endpoint is `storage.s3.endpoint`, not Spring Cloud AWS's `spring.cloud.aws.s3.endpoint`:
that one fails at startup when it is empty ("The URI scheme of endpointOverride must not be
null"). `ObjectStorageConfig` applies it only when set (blank means AWS S3 itself) and stops the
start with a clear message when it is not a URL, e.g. an unexpanded `${MINIO_URL}` from an env file.

`ObjectStorageConfig` also makes the client send and check only the
checksums S3 requires. The SDK's newer default checksums (CRC32 headers with chunked, trailing
signatures) are not supported by every S3-compatible server, and older Garage and SeaweedFS
releases reject such uploads.

Over HTTPS with a self-signed certificate, the certificate's *subject alternative names* must
include the storage host (e.g. `s3.example.com`, or `*.example.com`; Java ignores the common
name), and the API's JVM must trust it. See *Setting up Novu*, step 5, in `docs/novu/README.md`.

## Server side

What the API needs from the storage server, whichever it is:

- **A bucket**, `knowvera` by default. Create it on the server, or let the API create it on the
  first upload (`S3_CREATE_BUCKET=true`, needs a key that may create buckets).
- **A key** for the API with read, write, list and delete on that bucket.
- **Path-style access** at the endpoint (`https://s3.example.com/knowvera/<key>`); no wildcard DNS
  for virtual-host buckets is needed.
- **The region** the API sends (`S3_REGION`) must be the one the server expects.

Garage, for example:

```bash
garage bucket create knowvera
garage key create knowvera-api                      # prints the key id and secret once
garage bucket allow --read --write --owner knowvera --key knowvera-api
```

then `S3_ACCESS_KEY=<Key ID>`, `S3_SECRET_KEY=<Secret key>`, `S3_REGION=<s3_region of garage.toml>`,
`S3_CREATE_BUCKET=false` (Garage keys cannot create buckets unless given `--create-bucket`).

SeaweedFS: an identity in its S3 configuration (`-s3.config` JSON) with `Read`, `Write`, `List`
and `Admin` on `knowvera` (Admin lets the API create the bucket); any `S3_REGION`.

## Moving existing documents off MinIO

Documents store their object *name* (`Document.url`), not a server address, so copying the bucket
with the same names is the whole migration: no database change.

1. Stop uploads (stop the API, or pick a quiet period).
2. Copy the bucket with any S3 tool, e.g. the MinIO client `mc` (it works with any S3 server):

   ```bash
   mc alias set old https://minio.example.com <minio access key> <minio secret key>
   mc alias set new https://s3.example.com <new access key> <new secret key> --api s3v4 --path on
   mc mirror --preserve old/knowvera new/knowvera
   ```

   `rclone sync old:knowvera new:knowvera` does the same. Create the bucket first if the new key
   cannot.
3. Compare counts and sizes: `mc du old/knowvera` and `mc du new/knowvera`.
4. Set the `S3_*` variables and start the API; open a few documents to check they download.
5. Keep MinIO, read-only, until you are satisfied; then remove it and the `MINIO_*` variables.
