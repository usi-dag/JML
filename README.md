# JML
Benchmarking Java Machine Learning code with JMH and Java Vector API


#### Compile project
The following command clean the old target folder and compile with tests check the project.
```
mvn clean verify
```
#### jar execution with bytecode instructions
```
java  -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:PrintAssemblyOptions=intel -jar --add-modules jdk.incubator.vector target/benchmarks.jar > vectmp.txt
```

#### benchmark execution
The following command is an example to run the benchmark with 100 warmup iterations and 10 iteerations
considered in the benchmark.
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

### Possible Readings and Guidelines for intrinsic operations
- [chapter 12 using vector operations](https://www.agner.org/optimize/optimizing_cpp.pdf)  - agner.org
- [processor micro-architecture](https://www.agner.org/optimize/microarchitecture.pdf) - agner.org
- [instruction tables](https://www.agner.org/optimize/instruction_tables.pdf) - agner.org
- [instrinsic guide intel](https://software.intel.com/sites/landingpage/IntrinsicsGuide/#) - software.intel.com