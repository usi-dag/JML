# JML
Benchmarking Java Machine Learning code with JMH and Java Vector API

## Useful commands
- `-XX:UseAVX=0` Do not use vectorization optimization -> use of scalar operations
- `-XX:CompileThreshold=#`
#### jar execution with bytecode instructions
`java  -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:PrintAssemblyOptions=intel -jar --add-modules jdk.incubator.vector target/benchmarks.jar > vectmp.txt `

#### benchmark execution

`$java --add-modules jdk.incubator.vector -cp target/benchmarks.jar org.openjdk.jmh.Main -f 1 -wi 100 -i 10 -bm SingleShotTime`