package unit.SmartBPractice;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unit.SmartBPractice.config.DaoConfig;
import wen.SmartBPractice.model.Client;
import wen.SmartBPractice.repository.ClientRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = DaoConfig.class)
@Slf4j

public class RepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Before
    public void init(){
        Client c = Client.builder().name("CHT")
                .address("台北市").create_by("wen")
                .build();

        clientRepository.save(c);
        log.info("Client: {}", c);
    }

    @Test
    public void whenFindByAddress() {
        Client findC = clientRepository.findByAddress("台北市");
        log.info("Client: {}", findC);
        assertEquals("wen", findC.getCreate_by());
    }

}