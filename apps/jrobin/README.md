# JRobin (`jrobin/`)

Fork of the RRD4J library (Round-Robin Database for Java) for efficient time-series data storage and graphing. Used for router statistics in the console.

## What is Round-Robin Database?

RRD uses fixed-size databases that store data at regular intervals. Older data is automatically consolidated or discarded as new data arrives, ensuring:
- **Constant storage** - No growth over time
- **Efficient queries** - Pre-aggregated data at multiple resolutions
- **Graph-ready** - Built-in graphing from consolidated data

## How I2P Uses It

Router statistics ( bandwidth, peer count, tunnel usage) are logged at regular intervals. The console then graphs these:

```
Resolution    Storage    Retention (days)
-----------------------------------------
  1 min        ~400B      2 days
  5 min        ~400B     10 days  
  1 hour       ~400B     25 days
  1 day        ~400B    >1 year
```

## Data Consolidation

When moving between resolutions, RRD applies functions:
- `AVERAGE` - Mean of values
- `MAX` / `MIN` - Peak values
- `LAST` - Most recent value

This preserves both average behavior and peaks (important for bandwidth).

## Key Classes

| Class     | Purpose                     |
| --------- | --------------------------- |
| `RrdDb`   | Database, holds datasources |
| `RrdDef`  | Database definition         |
| `Sampler` | Insert values               |
| `Graph`   | Render to image             |

## Packages

- `org.rrd4j.core` - Database engine
- `org.rrd4j.graph` - Graph rendering
- `org.rrd4j.data` - Data manipulation and consolidation

## Building

```bash
# Gradle
./gradlew :apps:jrobin:jar

# Ant (from repo root)
ant buildJrobin
```