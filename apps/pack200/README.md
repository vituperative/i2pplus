# Pack200 (`pack200/`)

Fork of the Pack200 compression format for JAR files. Extracted from OpenJDK just before its removal in Java 14.

## Why Pack200?

Pack200 achieves 60-80% compression on JAR files by:
- Analyzing bytecode across all classes
- Sharing constant pools
- Applying predictions

This made it ideal for router updates over I2P (bandwidth-constrained). Development I2P+ updates use Pack200-compressed JARs.

## Status

Pack200 was **removed in Java 14** (2019). This fork re-adds Pack200 support for Java 14 and later, enabling compressed I2P+ updates across all supported Java versions.

## Packages

- `io.pack200` - Packer, unpacker, archive format

## Building

```bash
ant -f java/build.xml
```