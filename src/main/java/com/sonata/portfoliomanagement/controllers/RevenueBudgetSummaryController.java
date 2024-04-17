package com.sonata.portfoliomanagement.controllers;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sonata.portfoliomanagement.interfaces.AccountBudgetsRepository;
import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("revenuebudgets")
public class RevenueBudgetSummaryController {

	@Autowired
	RevenueBudgetSummaryRepository revenueRepo;
	@Autowired
	AccountBudgetsRepository accountBudgetsRepo;
	@Autowired
	DataEntryRepository dataEntryRepo;

	RestTemplate restTemplate = new RestTemplate();

	//method to add data into revenue table
	@PostMapping("/save")
	public ResponseEntity<RevenueBudgetSummary> createRevenueBudgetSummary(@RequestBody RevenueBudgetSummary revenue) {

		RevenueBudgetSummary createdRevenue = revenueRepo.save(revenue);

		return new ResponseEntity<>(createdRevenue, HttpStatus.CREATED);
	}

	@GetMapping("/get")
	public ResponseEntity<List<RevenueBudgetSummary>> getAllRevenueBudgets() {
		List<RevenueBudgetSummary> allRevenueBudgets = revenueRepo.findAll();
		return ResponseEntity.ok(allRevenueBudgets);
	}

	@GetMapping("/{id}")
	public RevenueBudgetSummary getbyid(@PathVariable int id) {
		return revenueRepo.findById(id).get();

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteRevenueVsBudgetById(@PathVariable int id) {
		if (revenueRepo.existsById(id)) {
			revenueRepo.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	//method to get only the list of DeliveryManagers present-->
	@GetMapping("/getdmlist")
	public List<String> getdeliveryManagers ()
	{
		List<String> dmList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			dmList.add(request.getDeliveryManager());
		}
		return dmList.stream().distinct().collect(Collectors.toList());
	}

	//This POST method puts the specific DMS required to get data from.Multiple DMs sent through the GET method below
	@PostMapping("/DM/post")
	public List<String> stringList(@RequestBody RevenueDTO dmList) {
		// Extract the list of strings from the RevenueDTO object
		List<String> stringList = dmList.getMyList();
		return stringList;
	}

	//The GET method calls the POST method above and need to send the request body for this get method to obtain for Multiple DMS
	@GetMapping("/DMs")
	public List<RevenueBudgetSummary> getRevenuebyDM(@RequestBody RevenueDTO dmList) {
		// Call the POST endpoint to get the list of strings
		String postUrl = "http://localhost:8081/revenuebudgets/DM/post";
		List<String> postResponse = restTemplate.postForObject(postUrl, dmList, List.class);

		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the response strings and filter Revenue objects accordingly
		for (String getList : postResponse) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByDeliveryManager(getList);
			resultList.addAll(filteredList);
		}

		return resultList;
	}

	//getting list for all accounts only-->
	@GetMapping("/getaccountlist")
	public List<String> getAccounts ()
	{
		List<String> accountList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			accountList.add(request.getAccount());
		}
		return accountList.stream().distinct().collect(Collectors.toList());
	}
	//This POST method puts the specific accounts required to get data from.Multiple accounts sent through the GET method below
	@PostMapping("/account/post")
	public List<String> stringListForAccount(@RequestBody RevenueDTO accountList) {
		// Extract the list of strings from the RevenueDTO object
		List<String> stringList = accountList.getMyList();
		return stringList;
	}

	//The GET method calls the POST method above and need to send the request body for this get method to obtain for Multiple accounts
	@GetMapping("/Accounts")
	public List<RevenueBudgetSummary> getRevenuebyAccount(@RequestBody RevenueDTO accountList) {
		// Call the POST endpoint to get the list of strings
		String postUrl = "http://localhost:8081/revenuebudgets/account/post";
		List<String> postResponse = restTemplate.postForObject(postUrl, accountList, List.class);

		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the response strings and filter Revenue objects accordingly
		for (String getList : postResponse) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByAccount(getList);
			resultList.addAll(filteredList);
		}

		return resultList;
	}

	//getting list for all project Managers only-->
	@GetMapping("/getpmlist")
	public List<String> getProjectManager ()
	{
		List<String> pmList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			pmList.add(request.getProjectManager());
		}
		return pmList.stream().distinct().collect(Collectors.toList());
	}

	//This POST method puts the specific DMS required to get data from.Multiple DMs sent through the GET method below
	@PostMapping("/pm/post")
	public List<String> pmstringList(@RequestBody RevenueDTO pmlist) {
		// Extract the list of strings from the RevenueDTO object
		List<String> pmstringList = pmlist.getMyList();
		return pmstringList;
	}

	//The GET method calls the POST method above and need to send the request body for this get method to obtain for Multiple DMS
	@GetMapping("/pms")
	public List<RevenueBudgetSummary> getRevenuebyPM(@RequestBody RevenueDTO pmlist) {
		// Call the POST endpoint to get the list of strings
		String postUrl = "http://localhost:8081/revenuebudgets/pm/post";
		List<String> postResponse = restTemplate.postForObject(postUrl, pmlist, List.class);

		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the response strings and filter Revenue objects accordingly
		for (String getList : postResponse) {
			List<RevenueBudgetSummary> filteredList =revenueRepo.findByprojectManager(getList);
			resultList.addAll(filteredList);
		}

		return resultList;
	}

	//getting list for all vertical only-->
	@GetMapping("/getverticallist")
	public List<String> getVerticalList()
	{
		List<String> verticalList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			verticalList.add(request.getVertical());
		}
		return verticalList.stream().distinct().collect(Collectors.toList());
	}

	@PostMapping("/vertical/post")
	public List<String> stringList2(@RequestBody RevenueDTO verticalList) {
		// Extract the list of strings from the RevenueDTO object
		List<String> stringList2 = verticalList.getMyList();
		return stringList2;
	}

	@GetMapping("/verticals")
	public List<RevenueBudgetSummary> getRevenueByVertical(@RequestBody RevenueDTO verticalList) {
		// Call the POST endpoint to get the list of strings
		String postUrl = "http://localhost:8081/revenuebudgets/vertical/post";
		List<String> postResponse = restTemplate.postForObject(postUrl, verticalList, List.class);

		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList2 = new ArrayList<>();

		// Iterate over the response strings and filter Revenue objects accordingly
		for (String getList : postResponse) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByVertical(getList);
			resultList2.addAll(filteredList);
		}

		return resultList2;
	}

	//List of all methods along with  all the data

	//getting classification list-->
	@GetMapping("/getclassificationlist")
	public List<String> getClassificationList()
	{
		List<String> classficationList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			classficationList.add(request.getClassification());
		}
		return classficationList.stream().distinct().collect(Collectors.toList());
	}

	@PostMapping("/classification/post")
	public List<String> stringList1(@RequestBody RevenueDTO classificationList) {
		// Extract the list of strings from the RevenueDTO object
		List<String> stringList1 = classificationList.getMyList();
		return stringList1;
	}

	@GetMapping("/classifications")
	public List<RevenueBudgetSummary> getRevenuebyClassification(@RequestBody RevenueDTO classificationList) {
		// Call the POST endpoint to get the list of strings
		String postUrl = "http://localhost:8081/revenuebudgets/classification/post";
		List<String> postResponse = restTemplate.postForObject(postUrl, classificationList, List.class);

		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList1 = new ArrayList<>();

		// Iterate over the response strings and filter Revenue objects accordingly
		for (String getList : postResponse) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByclassification(getList);
			resultList1.addAll(filteredList);
		}

		return resultList1;
	}

	//getting list for all projects only-->
	@GetMapping("/getprojectlist")
	public List<String> getProjectList()
	{
		List<String> projectList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			projectList.add(request.getProjectName());
		}
		return projectList.stream().distinct().collect(Collectors.toList());
	}

	@PostMapping("/project/post")
	public List<String> stringList3(@RequestBody RevenueDTO projectList) {
		// Extract the list of strings from the RevenueDTO object
		List<String> stringList3 = projectList.getMyList();
		return stringList3;
	}

	@GetMapping("/projects")
	public List<RevenueBudgetSummary> getRevenueByProject(@RequestBody RevenueDTO projectList) {
		// Call the POST endpoint to get the list of strings
		String postUrl = "http://localhost:8081/revenuebudgets/project/post";
		List<String> postResponse = restTemplate.postForObject(postUrl, projectList, List.class);

		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList3 = new ArrayList<>();

		// Iterate over the response strings and filter Revenue objects accordingly
		for (String getList : postResponse) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByProjectName(getList);
			resultList3.addAll(filteredList);
		}

		return resultList3;
	}

	//getting list for all years only-->
	@GetMapping("/getfinancialyearlist")
	public List<Integer> getFinancialYearList() {
		List<Integer> fyList = new ArrayList<>();
		List<RevenueBudgetSummary> revenueData = revenueRepo.findAll();
		for (RevenueBudgetSummary request : revenueData) {
			fyList.add(request.getFinancialYear());
		}
		return fyList.stream().distinct().collect(Collectors.toList());
	}

	//This POST method puts the specific FYs required to get data from.Multiple Fys sent through the GET method below
	@PostMapping("/FY/post")
	public List<Integer> fyIntegerList(@RequestBody RevenueDTO yearsList) {
		List<Integer> integerList = yearsList.getGetanotherList();
		return integerList;
	}

	@GetMapping("/FYs")
	public List<RevenueBudgetSummary> getRevenuebyFY(@RequestBody RevenueDTO yearsList) {
		String postUrl = "http://localhost:8081/revenuebudgets/FY/post";
		List<Integer> postResponse = restTemplate.postForObject(postUrl, yearsList, List.class);
		List<RevenueBudgetSummary> resultList = new ArrayList<>();
		for (Integer year : postResponse) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByFinancialYear(year);
			resultList.addAll(filteredList);
		}
		return resultList;
	}

	//get all quarters-->
	@GetMapping("/getquarterlist")
	public List<String> getQuarterList()
	{
		List<String> quarterList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			quarterList.add(request.getQuarter());
		}
		return quarterList.stream().distinct().collect(Collectors.toList());
	}

	//This POST method puts the specific quarters required to get data from.Multiple quarters sent through the GET method below
	@PostMapping("/quarter/post")
	public List<String> stringListForQuarter(@RequestBody RevenueDTO quarterList) {
		// Extract the list of strings from the RevenueDTO object
		List<String> stringList = quarterList.getMyList();
		return stringList;
	}

	//The GET method calls the POST method above and need to send the request body for this get method to obtain for Multiple quartersS
	@GetMapping("/quarters")
	public List<RevenueBudgetSummary> getRevenuebyquarter(@RequestBody RevenueDTO quarterlist) {
		// Call the POST endpoint to get the list of strings
		String postUrl = "http://localhost:8081/revenuebudgets/quarter/post";
		List<String> postResponse = restTemplate.postForObject(postUrl, quarterlist, List.class);

		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the response strings and filter Revenue objects accordingly
		for (String getList : postResponse) {
			List<RevenueBudgetSummary> filteredList =revenueRepo.findByquarter(getList);
			resultList.addAll(filteredList);
		}

		return resultList;
	}

	//get all Months-->
	@GetMapping("/getmonthlist")
	public List<Date> getMonth() {
		List<Date> monthList = new ArrayList<>();
		List<RevenueBudgetSummary> revenueData = revenueRepo.findAll();
		for (RevenueBudgetSummary request : revenueData) {
			monthList.add(request.getMonth());
		}
		return monthList.stream().distinct().collect(Collectors.toList());
	}

	//this method is going to add multiple years,accounts for a dm into a single array object instead of adding each account as a seperate object within the list-->
	@GetMapping("/ultraFilter")
	public ResponseEntity<List<RevenueBudgetSummary>> getByDMAccountYear(@RequestBody RevenueDTO dmList) {
		List<String> dmNames = dmList.getMyList();
		List<RevenueBudgetSummary> responseList = new ArrayList<>();

		for (String dmName : dmNames) {
			List<RevenueBudgetSummary> revenues = revenueRepo.findByDeliveryManager(dmName);
			responseList.addAll(revenues);
		}
		return ResponseEntity.ok(responseList);
	}

	//this method is getting the list of accounts only by giving the dm names in the request body in postman.this list to be sent in the next method connecting to it.
	@PostMapping("/accountbydm")
	public ResponseEntity<Set<String>> getAccountByDM(@RequestBody RevenueDTO dmList) {
		List<String> dmNames = dmList.getMyList();
		Set<String> account = new HashSet<>();
		List<RevenueBudgetSummary> revenues = revenueRepo.findByDeliveryManagerIn(dmNames);
		for (RevenueBudgetSummary revenue : revenues) {
			account.add(revenue.getAccount());
		}
		return ResponseEntity.ok(account);
	}


	@PostMapping("/projectbypm")
	public ResponseEntity<Set<String>> getprojectBypM(@RequestBody RevenueDTO pmList) {
		List<String> pmNames = pmList.getMyList();
		Set<String> project = new HashSet<>();

		List<RevenueBudgetSummary> revenues = revenueRepo.findByProjectManagerIn(pmNames);
		for (RevenueBudgetSummary revenue : revenues) {
			project.add(revenue.getProjectName());
		}


		return ResponseEntity.ok(project);
	}

	//list all pms under multiple account
	@PostMapping("/pmbyaccount")
	public ResponseEntity<Set<String>> getpmByacnt(@RequestBody RevenueDTO pmList){
		List<String>accountNames = pmList.getMyList();
		Set<String>  projectManager= new HashSet<>();

		List<RevenueBudgetSummary> revenues = revenueRepo.findByAccountIn(accountNames);
		for (RevenueBudgetSummary revenue : revenues) {
			projectManager.add(revenue.getProjectManager());
		}

		return ResponseEntity.ok(projectManager);
	}

	@PostMapping("/dmbyclassification")
	public ResponseEntity<Set<String>> getDMByClassification(@RequestBody RevenueDTO classificationList) {
		List<String> classificationNames = classificationList.getMyList();
		Set<String> dmNames = new HashSet<>();
		List<RevenueBudgetSummary> revenues = revenueRepo.findByClassificationIn(classificationNames); // Change method name
		for (RevenueBudgetSummary revenue : revenues) {
			dmNames.add(revenue.getDeliveryManager());
		}
		return ResponseEntity.ok(dmNames);
	}

	@PostMapping("/financialYearByProject")
	public ResponseEntity<Set<Integer>> getFinancialYearByProject(@RequestBody RevenueDTO projectList) {
		List<String> projectNames = projectList.getMyList();
		Set<Integer> financialYears = new HashSet<>();

		List<RevenueBudgetSummary> revenues = revenueRepo.findByProjectNameIn(projectNames);
		for (RevenueBudgetSummary revenue : revenues) {
			financialYears.add(revenue.getFinancialYear());
		}
		return ResponseEntity.ok(financialYears);
	}


	//In th request body for this method. we enter two things. vertical and classification and this will give the dm list under these specifics-->
	@PostMapping("/dmbyvertical&classification")
	public ResponseEntity<Set<String>> getDMByVerticalAndClassification(@RequestBody RevenueDTO dmList) {
		List<String> verticals = dmList.getVerticalList();
		List<String> classifications = dmList.getClassificationList();
		Set<String> deliveryManagerNames = new HashSet<>();

		List<RevenueBudgetSummary> revenues = revenueRepo.findByVerticalInAndClassificationIn(verticals, classifications);
		for (RevenueBudgetSummary revenue : revenues) {
			deliveryManagerNames.add(revenue.getDeliveryManager());
		}

		return ResponseEntity.ok(deliveryManagerNames);
	}

	//this method is to find dms using vertical only-->
	@PostMapping("/dmbyvertical")
	public ResponseEntity<Set<String>> getDMByVertical(@RequestBody RevenueDTO verticalList) {
		List<String> verticals = verticalList.getMyList();
		Set<String> deliveryManagerNames = new HashSet<>();

		List<RevenueBudgetSummary> revenues = revenueRepo.findByVerticalIn(verticals);
		for (RevenueBudgetSummary revenue : revenues) {
			deliveryManagerNames.add(revenue.getDeliveryManager());
		}

		return ResponseEntity.ok(deliveryManagerNames);
	}



	//This method takes quarter(Q1,Q2,Q3,Q4) and gives me their respective financial year's months-->
	@PostMapping("/monthsByQuarter")
	public ResponseEntity<Set<String>> getMonthsByQuarter(@RequestBody RevenueDTO quarterList) {
		List<String> quarters = quarterList.getMyList();
		Set<String> months = new HashSet<>();

		SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);

		for (String quarter : quarters) {
			switch (quarter) {
				case "Q1":
					months.addAll(getMonthsForQuarter(4, monthFormat));
					break;
				case "Q2":
					months.addAll(getMonthsForQuarter(7, monthFormat));
					break;
				case "Q3":
					months.addAll(getMonthsForQuarter(10, monthFormat));
					break;
				case "Q4":
					months.addAll(getMonthsForQuarter(1, monthFormat));
					break;
				default:
					System.out.println("Enter Valid Data!");
					break;
			}
		}

		return ResponseEntity.ok(months);
	}

	private List<String> getMonthsForQuarter(int startMonth, SimpleDateFormat monthFormat) {
		List<String> months = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, startMonth - 1); // Month in Java Calendar starts from 0

		for (int i = 0; i < 3; i++) {
			months.add(monthFormat.format(calendar.getTime()));
			calendar.add(Calendar.MONTH, 1);
		}

		return months;
	}



	//using this get method to get the data by using six filters.Only if a matching data for the specified request body exists in the database, it'll fetch that data or else it provides null
	@PostMapping("/financialYearByCriteria")
	public ResponseEntity<List<RevenueBudgetSummary>> getFinancialDataByCriteria(@RequestBody RevenueDTO criteria) {
		// Check if any field has been provided in the request
		if (criteria.isEmpty()) {
			// If no fields provided, return bad request
			return ResponseEntity.badRequest().build();
		}

		List<RevenueBudgetSummary> financialData = null;

		// Fetch data only if all criteria are provided and match
		if (areAllCriteriaMatched(criteria)) {
			financialData = fetchData(criteria);
		} else {
			// Indicate that the specified data doesn't exist
			financialData = Collections.emptyList(); // Or you can set it to null
		}

		return ResponseEntity.ok(financialData);
	}

	private boolean areAllCriteriaMatched(RevenueDTO criteria) {
		// Check if all criteria are provided and match
		return criteria.getFinancialYear() != null &&
				criteria.getProjectList() != null &&
				criteria.getVerticalList() != null &&
				criteria.getClassificationList() != null &&
				criteria.getDmList() != null &&
				criteria.getAccountList() != null &&
				criteria.getPmList() != null;
	}


	//creating a criteria for specifying by quarters as well as a filter-->
	@PostMapping ("/quarterByCriteria")
	public ResponseEntity<List<RevenueBudgetSummary>> getFinancialDataByCriteriaofQuarter(@RequestBody RevenueDTO criteria1) {
		// Check if any field has been provided in the request
		if (criteria1.isEmpty()) {
			// If no fields provided, return bad request
			return ResponseEntity.badRequest().build();
		}

		List<RevenueBudgetSummary> financialData1 = null;

		// Fetch data only if all criteria are provided and match
		if (areAllCriteriaMatchedforQuarter(criteria1)) {
			financialData1 = fetchData(criteria1);
		} else {
			// Indicate that the specified data doesn't exist
			financialData1 = Collections.emptyList(); // Or you can set it to null
		}

		return ResponseEntity.ok(financialData1);
	}

	private boolean areAllCriteriaMatchedforQuarter(RevenueDTO criteria1) {
		// Check if all criteria are provided and match
		return criteria1.getFinancialYear() != null &&
				criteria1.getProjectList() != null &&
				criteria1.getVerticalList() != null &&
				criteria1.getClassificationList() != null &&
				criteria1.getDmList() != null &&
				criteria1.getAccountList() != null &&
				criteria1.getPmList() != null &&
				criteria1.getQuarterList() != null;
	}

	private List<RevenueBudgetSummary> fetchData(RevenueDTO criteria) {
		// Fetch data from the repository based on the provided criteria
		return revenueRepo.findByFinancialYearInAndProjectNameInAndVerticalInAndClassificationInAndDeliveryManagerInAndAccountInAndProjectManagerInAndQuarterIn(
				criteria.getFinancialYear(),
				criteria.getProjectList(),
				criteria.getVerticalList(),
				criteria.getClassificationList(),
				criteria.getDmList(),
				criteria.getAccountList(),
				criteria.getPmList(),
				criteria.getQuarterList());

	}

	private List<RevenueBudgetSummary> fetchData1(RevenueDTO criteria1) {
		// Fetch data from the repository based on the provided criteria
		return revenueRepo.findByFinancialYearInAndProjectNameInAndVerticalInAndClassificationInAndDeliveryManagerInAndAccountInAndProjectManagerInAndQuarterIn(
				criteria1.getFinancialYear(),
				criteria1.getProjectList(),
				criteria1.getVerticalList(),
				criteria1.getClassificationList(),
				criteria1.getDmList(),
				criteria1.getAccountList(),
				criteria1.getPmList(),
				criteria1.getQuarterList()
		);
	}


	@GetMapping("/dm/{deliveryManager}")
	public List<RevenueBudgetSummary> getRevenuesByDM(@PathVariable("deliveryManager") String deliveryManager) {
		List<RevenueBudgetSummary> revenues = revenueRepo.findBydeliveryManager(deliveryManager);
		return revenues;
	}

	@GetMapping("/quarter/{quarter}")
	public List<RevenueBudgetSummary> getRevenuesByquarter(@PathVariable("quarter") String quarter) {
		List<RevenueBudgetSummary> revenues = revenueRepo.findByquarter(quarter);
		return revenues;
	}

	@GetMapping("/month/{month}")
	public List<RevenueBudgetSummary> getRevenuesByMonth(@PathVariable("month") int month) {
		// Assuming month corresponds to financial year
		List<RevenueBudgetSummary> revenues = revenueRepo.findByFinancialYear(month);
		return revenues;
	}

	@GetMapping("/account/{account}")
	public List getRevenuesByAccount(@PathVariable("account") String account) {
		List revenues = revenueRepo.findByAccount(account);
		return revenues;
	}

	@GetMapping("/financialYear/{year}")
	public List getByFinancialYear(@PathVariable("year") int year) {
		return revenueRepo.findByFinancialYear(year);
	}
	@GetMapping("/vertical/{vertical}")
	public List<RevenueBudgetSummary> getByVertical(@PathVariable("vertical") String vertical) {
		return revenueRepo.findAllByVertical(vertical);
	}

	@GetMapping("/projectManager/{projectManager}")
	public List<RevenueBudgetSummary> getProjectManagerByName(@PathVariable("projectManager") String projectManager) {
		return revenueRepo.findByProjectManager(projectManager);
	}

	@GetMapping("/classification/{classification}")
	public List<RevenueBudgetSummary> getByClassification(@PathVariable String classification) {
		return revenueRepo.findAllByClassification(classification);
	}

	@GetMapping("/projectName/{projectName}")
	public List<RevenueBudgetSummary> getRevenueByProjectName(@PathVariable("projectName") String projectName) {
		return revenueRepo.findByProjectName(projectName);
	}

}

