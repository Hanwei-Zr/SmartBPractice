package wen.SmartBPractice.test;

import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wen.SmartBPractice.config.DaoConfig;
import wen.SmartBPractice.model.Company;
import wen.SmartBPractice.repository.CompanyRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(classes = DaoConfig.class)
public class SmartPracticeApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private HttpHeaders httpHeaders;
	private MockHttpSession session;

	@Autowired
	CompanyRepository companyRepository;

	private void createCompany(String name, String address) {
		Company c = new Company();
		c.setName(name);
		c.setAddress(address);
		companyRepository.save(c);
	}

	@Before
	public void init() throws Exception {
		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		session = new MockHttpSession();

		JSONObject request = new JSONObject();
		request.put("account", "admin");
		request.put("password", "admin");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.post("/api/login")
						.headers(httpHeaders)
						.session(session)
						.content(request.toString());

		mockMvc.perform(requestBuilder)
//				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("authority").hasJsonPath())
				.andExpect(jsonPath("authority").value("ROLE_ADMIN"));
	}

	@After
	public void clear() throws Exception {

		companyRepository.deleteAll();

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.get("/api/logout")
						.headers(httpHeaders);


		mockMvc.perform(requestBuilder)
//				.andDo(print())
				.andExpect(status().isOk());
	}


	@Test
	public void testCreate() throws Exception {

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		JSONObject request = new JSONObject();
		request.put("name", "CHT");
		request.put("address", "Taiwan");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.post("/api/company/create")
						.headers(httpHeaders)
						.session(session)
						.content(request.toString());

		mockMvc.perform(requestBuilder)
//				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").hasJsonPath())
				.andExpect(jsonPath("name").value("CHT"))
				.andExpect(jsonPath("address").value("Taiwan"));
	}

	@Test
	public void testView() throws Exception {
		createCompany("CHT", "Taiwan");

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.get("/api/company/view")
						.session(session)
						.headers(httpHeaders);

		mockMvc.perform(requestBuilder)
//				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void testModify() throws Exception {
		createCompany("CHT", "Taiwan");

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		JSONObject request = new JSONObject();
		request.put("name", "Modify");
		request.put("address", "TaiwanC");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.post("/api/company/modify/4")
						.headers(httpHeaders)
						.session(session)
						.content(request.toString());

		mockMvc.perform(requestBuilder)
//				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").hasJsonPath())
				.andExpect(jsonPath("name").value("Modify"))
				.andExpect(jsonPath("address").value("TaiwanC"));
	}

	@Test(expected = RuntimeException.class)
	public void testDelete() throws Exception {
		createCompany("CHT", "Taiwan");

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.get("/api/company/delete/1")
						.session(session)
						.headers(httpHeaders);

		mockMvc.perform(requestBuilder);


		companyRepository.findById(Long.parseLong("1"))
				.orElseThrow(RuntimeException::new);

	}
}
