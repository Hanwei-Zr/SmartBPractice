package wen.SmartBPractice.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wen.SmartBPractice.config.DaoConfig;
import wen.SmartBPractice.model.Client;
import wen.SmartBPractice.model.Company;
import wen.SmartBPractice.repository.ClientRepository;
import wen.SmartBPractice.repository.CompanyRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = DaoConfig.class)
@Slf4j

public class RepositoryTest {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Before
    public void init(){
        Company c = Company.builder().name("CHT")
                .address("台北市").create_by("wen")
                .build();

        companyRepository.save(c);
        log.info("Company: {}", c);
    }

    @Test
    public void whenFindByAddress() {
        Company findC = companyRepository.findByAddress("台北市");
        log.info("find Address: {}", findC);
        //assertEquals("wen", findC.getCreate_by());

        companyRepository.delete(findC);

        Company c = Company.builder().name("ZZZHAS")
                .address("台北新北EEE").create_by("WW")
                .build();

        companyRepository.save(c);

        List<Company> list = companyRepository.findAll();
        log.info("List All Company: {}", list);

    }

    @Test
    public void whenCreateClient() {
        Client c = Client.builder()
                //.company_id("12341213")
                .name("wenwen")
                .email("wen81324@gmail")
                .phone("1234556789")
                .create_by("wen")
                .build();

        clientRepository.save(c);
        log.info("Create Client: {}", c);
    }
}