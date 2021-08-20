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

#### Results
###### Linear Regression
```
Benchmark                                                   Mode  Cnt     Score    Error  Units
BenchmarkLinearRegression.testDoubleLinearRegression          ss   10  1479.897 ± 11.109  ms/op
BenchmarkLinearRegression.testDoubleVectorLinearRegression    ss   10  1062.418 ±  5.506  ms/op
BenchmarkLinearRegression.testIntLinearRegression             ss   10   716.094 ±  7.511  ms/op
BenchmarkLinearRegression.testIntVectorLinearRegression       ss   10   445.963 ±  5.477  ms/op
BenchmarkLinearRegression.testLongLinearRegression            ss   10  1517.387 ± 18.518  ms/op
BenchmarkLinearRegression.testLongVectorLinearRegression      ss   10  1151.612 ± 13.486  ms/op

```

### Readings
- [chapter 12 using vector operations](https://www.agner.org/optimize/optimizing_cpp.pdf)  - agner.org
- [processor micro-architecture](https://www.agner.org/optimize/microarchitecture.pdf) - agner.org
- [instruction tables](https://www.agner.org/optimize/instruction_tables.pdf) - agner.org
- [instrinsic guide intel](https://software.intel.com/sites/landingpage/IntrinsicsGuide/#) - software.intel.com
- [VCL library for GNU compiler](https://raw.githubusercontent.com/vectorclass/manual/master/vcl_manual.pdf)


https://linuxize.com/post/how-to-install-apache-maven-on-ubuntu-20-04/
https://java.tutorials24x7.com/blog/how-to-install-openjdk-16-on-ubuntu-20-04-lts