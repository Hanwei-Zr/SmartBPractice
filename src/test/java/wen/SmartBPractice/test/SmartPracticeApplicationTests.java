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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wen.SmartBPractice.model.Client;
import wen.SmartBPractice.model.Company;
import wen.SmartBPractice.repository.ClientRepository;
import wen.SmartBPractice.repository.CompanyRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SmartPracticeApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private HttpHeaders httpHeaders;
	private MockHttpSession session;

	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	ClientRepository clientRepository;

	private void createCompany(String name, String address) {
		Company c = new Company();
		c.setName(name);
		c.setAddress(address);
		companyRepository.save(c);
	}

	private void createClient(Company company, String name, String email, String phone) {
		Client c = new Client();
		c.setCompany(company);
		c.setEmail(email);
		c.setName(name);
		c.setPhone(phone);
		clientRepository.save(c);
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
		clientRepository.deleteAll();

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
	public void testCompanyCreate() throws Exception {

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
	public void testCompanyView() throws Exception {
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
	public void testCompanyModify() throws Exception {
		createCompany("CHT", "Taiwan");

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		JSONObject request = new JSONObject();
		request.put("name", "Modify");
		request.put("address", "TaiwanC");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.post("/api/company/modify/" + companyRepository.findTopByOrderById().getId())
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
	public void testCompanyDelete() throws Exception {
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

	@Test
	public void testClientCreate() throws Exception {
		createCompany("DAS", "JP");

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		JSONObject request = new JSONObject();
		request.put("company_id", "5");
		request.put("name", "wen");
		request.put("email", "wen@das.co");
		request.put("phone", "0987654321");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.post("/api/client/create")
						.headers(httpHeaders)
						.session(session)
						.content(request.toString());

		mockMvc.perform(requestBuilder)
//				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").hasJsonPath())
				.andExpect(jsonPath("name").value("wen"))
				.andExpect(jsonPath("email").value("wen@das.co"))
				.andExpect(jsonPath("phone").value("0987654321"));
	}

	@Test
	public void testClientView() throws Exception {
		createCompany("CHT", "Taiwan");

		Company c = companyRepository.findTopByOrderById();
		createClient(c, "wen", "testView", "1234567890");

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.get("/api/client/view")
						.session(session)
						.headers(httpHeaders);

		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(status().isOk());
	}

//	@Test
//	public void testCompanyModify() throws Exception {
//		createCompany("CHT", "Taiwan");
//
//		httpHeaders = new HttpHeaders();
//		httpHeaders.add("Content-Type", "application/json");
//
//		JSONObject request = new JSONObject();
//		request.put("name", "Modify");
//		request.put("address", "TaiwanC");
//
//		RequestBuilder requestBuilder =
//				MockMvcRequestBuilders
//						.post("/api/company/modify/4")
//						.headers(httpHeaders)
//						.session(session)
//						.content(request.toString());
//
//		mockMvc.perform(requestBuilder)
////				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("id").hasJsonPath())
//				.andExpect(jsonPath("name").value("Modify"))
//				.andExpect(jsonPath("address").value("TaiwanC"));
//	}
//
//	@Test(expected = RuntimeException.class)
//	public void testCompanyDelete() throws Exception {
//		createCompany("CHT", "Taiwan");
//
//		httpHeaders = new HttpHeaders();
//		httpHeaders.add("Content-Type", "application/json");
//
//		RequestBuilder requestBuilder =
//				MockMvcRequestBuilders
//						.get("/api/company/delete/1")
//						.session(session)
//						.headers(httpHeaders);
//
//		mockMvc.perform(requestBuilder);
//		companyRepository.findById(Long.parseLong("1"))
//				.orElseThrow(RuntimeException::new);
//	}

}
