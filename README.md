# JML
Benchmarking Java Machine Learning code with JMH and Java Vector API

## Useful commands
- `-XX:UseAVX=0` Do not use vectorization optimization -> use of scalar operations
- `-XX:CompileThreshold=#`
#### jar execution with bytecode instructions
```
java  -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:PrintAssemblyOptions=intel -jar --add-modules jdk.incubator.vector target/benchmarks.jar > vectmp.txt
```

#### benchmark execution

```
java --add-modules jdk.incubator.vector -cp target/benchmarks.jar org.openjdk.jmh.Main -f 1 -wi 100 -i 10 -bm SingleShotTime
```

### Readings
- [chapter 12 using vector operations](https://www.agner.org/optimize/optimizing_cpp.pdf)  - agner.org
- [processor micro-architecture](https://www.agner.org/optimize/microarchitecture.pdf) - agner.org
- [instruction tables](https://www.agner.org/optimize/instruction_tables.pdf) - agner.org
- [instrinsic guide intel](https://software.intel.com/sites/landingpage/IntrinsicsGuide/#) - software.intel.com
- [VCL library for GNU compiler](https://raw.githubusercontent.com/vectorclass/manual/master/vcl_manual.pdf)