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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CrudAdminTest {

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

	private void createClient(String name, String email, String phone) {
		createCompany("testInClient", "Taiwan");
		Company company = companyRepository.findTopByOrderById();

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
		long id = companyRepository.findTopByOrderById().getId();
		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.get("/api/company/delete/" + id)
						.session(session)
						.headers(httpHeaders);

		mockMvc.perform(requestBuilder);
		companyRepository.findById(id)
				.orElseThrow(RuntimeException::new);
	}

	@Test
	public void testClientCreate() throws Exception {
		createCompany("DAS", "JP");
		long id = companyRepository.findTopByOrderById().getId();

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		JSONObject request = new JSONObject();
		request.put("company_id", id);
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
		createClient("TestView", "testView", "1234567890");

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

	@Test
	public void testClientModify() throws Exception {
		createClient("testModify", "wen@gmail.com", "1234567890");

		Client c  = clientRepository.findTopByOrderById();
		long id = c.getId();
		long company_id = c.getCompany().getId();

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		JSONObject request = new JSONObject();
		request.put("name", "AfterModify");
		request.put("email", "weee@c");
		request.put("phone", "1234567890");
		request.put("company_id", company_id);

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.post("/api/client/modify/" + id)
						.headers(httpHeaders)
						.session(session)
						.content(request.toString());

		mockMvc.perform(requestBuilder)
//				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("name").value("AfterModify"));
	}

	@Test(expected = RuntimeException.class)
	public void testClientDelete() throws Exception {
		createClient("testDel", "wen@gmail.com", "1234567890");

		long id = clientRepository.findTopByOrderById().getId();

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.get("/api/client/delete/" + id)
						.session(session)
						.headers(httpHeaders);

		mockMvc.perform(requestBuilder);
		clientRepository.findById(id)
				.orElseThrow(RuntimeException::new);
	}

	@Test
	public void testMutiClientCreate() throws Exception {
		createCompany("DAS", "JP");
		long id = companyRepository.findTopByOrderById().getId();

		httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");

		List<JSONObject> requestList = new ArrayList<>();

		JSONObject request1 = new JSONObject();
		request1.put("company_id", id);
		request1.put("name", "wen");
		request1.put("email", "wen@das.co");
		request1.put("phone", "0987654321");

		requestList.add(request1);

		JSONObject request2 = new JSONObject();
		request2.put("company_id", id);
		request2.put("name", "testest");
		request2.put("email", "wen@222das.co");
		request2.put("phone", "098337654321");

		requestList.add(request2);

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.post("/api/client/create/multi")
						.headers(httpHeaders)
						.session(session)
						.content(requestList.toString());

		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").exists())
				.andExpect(jsonPath("$[0].name").value("wen"))
				.andExpect(jsonPath("$[0].email").value("wen@das.co"))
				.andExpect(jsonPath("$[0].phone").value("0987654321"))
				.andExpect(jsonPath("$[1].name").value("testest"))
				.andExpect(jsonPath("$[1].email").value("wen@222das.co"))
				.andExpect(jsonPath("$[1].phone").value("098337654321"));
	}

}
