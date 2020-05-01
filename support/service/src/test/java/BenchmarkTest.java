import com.github.charlesvhe.support.SupportApplication;
import com.github.charlesvhe.support.entity.ConfigMeta;
import com.github.charlesvhe.support.service.Benchmark;
import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(classes = SupportApplication.class)
public class BenchmarkTest {
    @Autowired
    private Benchmark benchmark;

    private Long id = 100L;
    private Date date = new Date();

    @JunitPerfConfig(threads = 1, warmUp = 10000, duration = 30000)
    public void benchmarkTest() {
        // 测试结论 QueryDSL比MyBatis性能高5%左右
        // 性能高出部分主要是产生SQL时QueryDSL有meta信息可以用StringBuffer生成SQL而MyBatis必须使用反射或模板引擎来产生SQL

        // 测试查询
//        benchmark.testMybatisQuery();   // 72723,60218,60835,63600,72872
//        benchmark.testQuerydslQuery();  // 76107,75195,62907,77165,76712

        ConfigMeta configMeta = new ConfigMeta();
        configMeta.setId(id++);
        configMeta.setAppId("appId");
        configMeta.setCode("code");
        configMeta.setProperty("property");
        configMeta.setColumnName("columnName");
        configMeta.setDescription("description");
        configMeta.setSort((short) 0);
        configMeta.setGmtCreate(date);
        configMeta.setGmtModified(date);

        // 测试插入
//        benchmark.testMybatisSave(configMeta);  // 38493,38377,36259,37182,38477
//        benchmark.testQuerydslSave(configMeta); // 38690,38872,38377,37970,38709

        // 测试修改
//        configMeta.setId(configMeta.getId() % 10 + 11);

//        benchmark.testMybatisUpdate(configMeta);  // 38089,37832,37245,37383,38226
//        benchmark.testQuerydslUpdate(configMeta);   // 38842,38308,38494,38293,38296
    }
}
