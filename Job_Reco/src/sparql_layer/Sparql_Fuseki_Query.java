package sparql_layer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import model.Applicants;
import model.Jobs;
import model.LatLongPair;
import model.Location;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Sparql_Fuseki_Query {

	static String serviceEndpoint = "http://localhost:3030/ExploreJob/";
	// static String serviceEndpoint =
	// "https://ser531.s3.amazonaws.com/Location.txt?force=true";
   
	
	
	
	public static void main(String[] args) throws IOException, ParseException {

		StringBuilder queryString = new StringBuilder();
		queryString.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdf-schema#" + "> ");
		queryString.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
		queryString.append("PREFIX getApp: <http://www.semanticweb.org/SER-531/Team-14/Applicants#>");
		queryString.append("PREFIX getJob: <http://www.semanticweb.org/SER-531/Team-14/Jobs#>");
		queryString.append("PREFIX getLoc: <http://www.semanticweb.org/SER-531/Team-14/Location#>");

		Sparql_Fuseki_Query sfq = new Sparql_Fuseki_Query();
		// sfq.getCityName(serviceEndpoint, queryString);
		// sfq.solve1(serviceEndpoint, queryString);

		String json = "{\"skills\":[\"communication\"]}";
//		sfq.getJobByFilter(queryString, json);
		sfq.getApplicantByFilter(queryString, json);

		// System.out.println(json);
	}

	private StringBuilder createApplicantFilter(StringBuilder query, String json) throws ParseException {
		Object obj = new JSONParser().parse(json);
		JSONObject jo = (JSONObject) obj;
		if (jo.get("email") != null) {
			String value = jo.get("title").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("expectedGraduationDate") != null) {
			String value = jo.get("expectedGraduationDate").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("expectedSalary") != null) {
			String value = jo.get("postdate").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("applicantName") != null) {
			String value = jo.get("appdeadline").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		
		if (jo.get("specialzation") != null) {
			String value = jo.get("specialzation").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("schoolLevel") != null) {
			String value = jo.get("schoolLevel").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		

		query.append("} LIMIT 25");

		return query;

	}

	private StringBuilder createJobFilter(StringBuilder query, String json) throws ParseException {
		Object obj = new JSONParser().parse(json);
		JSONObject jo = (JSONObject) obj;

		if (jo.get("title") != null) {
			String value = jo.get("title").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("cityName") != null) {
			String value = jo.get("cityName").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("postdate") != null) {
			String value = jo.get("postdate").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("appdeadline") != null) {
			String value = jo.get("appdeadline").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("department") != null) {
			String value = jo.get("department").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("specialzationRequirement") != null) {
			String value = jo.get("specialzationRequirement").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("graduateLevelReq") != null) {
			String value = jo.get("graduateLevelReq").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}
		if (jo.get("posteBy") != null) {
			String value = jo.get("postedBy").toString();
			String titleFilter = "FILTER (?title = \"" + value + "\")";
			query.append(titleFilter);
		}

		query.append("} LIMIT 25");

		return query;
	}

	private StringBuilder createApplicantFilterSkill(StringBuilder query, String json) throws ParseException {
		Object obj = new JSONParser().parse(json);
		JSONObject jo = (JSONObject) obj;
		if (jo.get("skills") != null) {
			JSONArray ja = (JSONArray) jo.get("skills");
			Iterator itr2 = ja.iterator();
			while (itr2.hasNext()) {
				String str = itr2.next().toString();
				query.append("FILTER regex(?skills, \"" + str + "\")");

			}
		}

		return query;
	}

	private StringBuilder createJobFilterSkill(StringBuilder query, String json) throws ParseException {
		Object obj = new JSONParser().parse(json);
		JSONObject jo = (JSONObject) obj;
		if (jo.get("skillName") != null) {
			JSONArray ja = (JSONArray) jo.get("skillName");
			Iterator itr2 = ja.iterator();
			while (itr2.hasNext()) {
				String str = itr2.next().toString();
				query.append("FILTER regex(?skillName,\"" + str + "\")");

			}
		}

		return query;
	}

	public void getApplicantByFilter(StringBuilder queryString, String json) throws ParseException {
		queryString.append(
				"select ?name ?email ?gender ?expectedGradDate ?expectedSalary ?skills ?major ?university ?schoolLevel ?specialization ?loc ?lat ?long\r\n"
						+ "WHERE{\r\n" + "?person    getApp:has_Name  ?name ;\r\n"
						+ "     	    getApp:lives_in  ?loc ;\r\n" + "           getApp:email  ?email ;\r\n"
						+ "           getApp:gender ?gender ;\r\n"
						+ "     	    getApp:expectedGraduationDate  ?expectedGradDate ;\r\n"
						+ "           getApp:expected_Salary  ?expectedSalary ;\r\n"
						+ "     	    getApp:has_skills  ?skills ;\r\n" + "           getApp:major  ?major ;\r\n"
						+ "     	    getApp:school  ?university ;\r\n"
						+ "           getApp:schoolLevel ?schoolLevel ;\r\n"
						+ "  		    getApp:specialization ?specialization ;\r\n"
						+ "		    getApp:schoolLevel \"Bachelor\" .\r\n" + "?location   getLoc:has_Name  ?loc ;\r\n"
						+ "      getLoc:has_Latitude ?lat ;\r\n" + "      getLoc:has_Longitude ?long\n");
		queryString = createApplicantFilterSkill(queryString, json);
		queryString = createApplicantFilter(queryString, json);
		Query query = QueryFactory.create(queryString.toString());
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query);
		try {
			ResultSet response = qexec.execSelect();
			List<Applicants> result = getApplicantFromResponse(response);
			System.out.println(result);

		} finally {
			qexec.close();
		}
	}

	public void getJobByFilter(StringBuilder queryString, String json) throws ParseException {
		queryString.append(
				"select distinct ?title ?cityName ?postdate ?appdeadline ?skillName ?postedBy ?department ?specialzationRequirement ?graduateLevelReq ?lat ?long ?salary\n"
						+ "WHERE{\n" + "?s  getJob:has_title ?title ;\n" + "    getJob:located_in ?cityName ;\n"
						+ "    getJob:posting_date ?postdate ;\n" + "    getJob:application_Deadline ?appdeadline ;\n"
						+ "    getJob:belongs_to ?department ;\n" + "    getJob:posted_by ?postedBy ;\n"
						+ "    getJob:has_SkillName ?skillName ;\n"
						+ "    getJob:specialzationRequirement ?specialzationRequirement ;\n"
						+ "    getJob:graduateLevelRequirement ?graduateLevelReq ;\n"
						+ "    getJob:has_Salary ?salary .\n" + "  \n" + "?s1 getLoc:has_Name ?cityName ;\n"
						+ "    getLoc:has_Latitude ?lat ;\n" + "    getLoc:has_Longitude ?long\n");

		queryString = createJobFilterSkill(queryString, json);
		queryString = createJobFilter(queryString, json);
		Query query = QueryFactory.create(queryString.toString());
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query);
		try {
			ResultSet response = qexec.execSelect();
			List<Jobs> result = getJobFromResponse(response);
			System.out.println(result);

		} finally {
			qexec.close();
		}
	}

	private List<Applicants> getApplicantFromResponse(ResultSet response) {
		ArrayList<Applicants> list = new ArrayList<Applicants>();
		while (response.hasNext()) {
			QuerySolution soln = response.nextSolution();
			RDFNode email = soln.get("?email");
			RDFNode expectedGraduationDate = soln.get("?expectedGraduationDate");
			RDFNode expectedSalary = soln.get("?expectedSalary");
			RDFNode gender = soln.get("?gender");
			RDFNode applicantName = soln.get("?applicantName");
			RDFNode lives_in = soln.get("?lives_in");
			RDFNode major = soln.get("?major");
			RDFNode university = soln.get("?university");
			RDFNode specialization = soln.get("?specialization");
			RDFNode latitude = soln.get("?lat");
			RDFNode longitude = soln.get("?long");
			RDFNode schoolLevel = soln.get("?schoolLevel");
			RDFNode skills = soln.get("skills");

			Applicants applicant = new Applicants();
			Location location = new Location();
			LatLongPair pair = new LatLongPair();

			if (skills != null) {
				String skill_string = skills.toString();

				List<String> skill_list = Arrays.asList(skill_string.split(","));
				applicant.setSkills(skill_list);
			} else {
				System.out.println("Skills Not Found");
			}

			if (email != null) {
				applicant.setEmail(email.toString());
			} else {
				System.out.println("Email  Not Found");
			}
			if (expectedGraduationDate != null)
				applicant.setExpectedGraduationDate(expectedGraduationDate.toString());
			else
				System.out.println("Expected Graduation Date Not Found");

			if (lives_in != null)
				location.setCityName(lives_in.toString());
			else
				System.out.println("City Name Not Found");

			if (gender != null)
				applicant.setGender(gender.toString());
			else
				System.out.println("Gender Not Mentioned");
			if (major != null)
				applicant.setMajor(major.toString());
			else
				System.out.println("Major Not Found");
			if (schoolLevel != null)
				applicant.setSchoolLevel(schoolLevel.toString());
			else
				System.out.println("School Level Not Found");
			if (specialization != null)
				applicant.setSpecialization(specialization.toString());
			else
				System.out.println("Specialization Not Found");

			if (latitude != null)
				pair.setLatitude(latitude.toString());
			else
				System.out.println("Latitude Not found");
			if (longitude != null)
				pair.setLongitude(longitude.toString());
			else
				System.out.println("Longitude Not Found");

			location.setPair(pair);
			applicant.setLocation(location);

			list.add(applicant);

		}
		return list;

	}

	private List<Jobs> getJobFromResponse(ResultSet response) {
		ArrayList<Jobs> list = new ArrayList<Jobs>();
		while (response.hasNext()) {
			QuerySolution soln = response.nextSolution();
			RDFNode title = soln.get("?title");
			RDFNode cityName = soln.get("?cityName");
			RDFNode postingdate = soln.get("?postdate");
			RDFNode applicationDeadLine = soln.get("?appdeadline");
			RDFNode department = soln.get("?department");
			RDFNode specializationReq = soln.get("?specialzationRequirement");
			RDFNode jobSalary = soln.get("?salary");
			RDFNode graduateLevelReq = soln.get("?graduateLevelReq");
			RDFNode latitude = soln.get("?lat");
			RDFNode longitude = soln.get("?long");
			RDFNode postedBy = soln.get("?postedBy");
			RDFNode skills = soln.get("skillName");

			Jobs job = new Jobs();
			Location location = new Location();
			LatLongPair pair = new LatLongPair();

			if (skills != null) {
				String skill_string = skills.toString();

				List<String> skill_list = Arrays.asList(skill_string.split(","));
				job.setSkillName(skill_list);
			} else {
				System.out.println("Skills Not Found");
			}

			if (postedBy != null) {
				job.setPostedBy(postedBy.toString());
			} else {
				System.out.println("Posted By Not Found");
			}
			if (title != null)
				job.setTitle(title.toString());
			else
				System.out.println("Title Not Found");

			if (cityName != null)
				location.setCityName(cityName.toString());
			else
				System.out.println("City Name Not Found");

			if (postingdate != null)
				job.setPostingdate(postingdate.toString());
			else
				System.out.println("Posting Date Not Found");
			if (applicationDeadLine != null)
				job.setApplicationDeadLine(applicationDeadLine.toString());
			else
				System.out.println("Application Deadline Not Found");
			if (department != null)
				job.setDepartment(department.toString());
			else
				System.out.println("Department Not Found");
			if (specializationReq != null)
				job.setSpecializationReq(specializationReq.toString());
			else
				System.out.println("Special Requirement Not Found");
			if (jobSalary != null)
				job.setJobSalary(jobSalary.toString());
			else
				System.out.println("Job Salary Not Found");
			if (graduateLevelReq != null)
				job.setGraduateLevelReq(graduateLevelReq.toString());
			else
				System.out.println("Graduate Level Requirement Not Found");
			if (latitude != null)
				pair.setLatitude(latitude.toString());
			else
				System.out.println("Latitude Not found");
			if (longitude != null)
				pair.setLongitude(longitude.toString());
			else
				System.out.println("Longitude Not Found");

			location.setPair(pair);
			job.setLocation(location);

			list.add(job);

		}
		return list;

	}

	private void getCityName(String serviceEndpoint2, StringBuilder queryString) {
		queryString.append("SELECT DISTINCT ?o WHERE {?s getLoc:has_Name ?o FILTER(?o = \"Miami\")} LIMIT 2");
		Query query = QueryFactory.create(queryString.toString());
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint2, query);

		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?o");
				if (name != null)
					System.out.println("City -  " + name.toString());
				else
					System.out.println("Not Found!");

			}
		} finally {
			qexec.close();
		}

	}

	private void solve1(String serviceEndpoint, StringBuilder queryString) {
		queryString.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX getApp: <http://www.semanticweb.org/SER-531/Team-14/Applicants#>\n"
				+ "PREFIX getLoc: <http://www.semanticweb.org/SER-531/Team-14/Location#>\n" + "\n"
				+ "select ?name ?loc ?lat ?long\n" + "WHERE{\n" + "?s getApp:has_Name ?name .\n"
				+ "?s getApp:lives_in ?loc .\n" + "?s getApp:schoolLevel \"Bachelor\" .\n"
				+ "?s1 getLoc:has_Name ?loc .\n" + "?s1 getLoc:has_Latitude ?lat .\n"
				+ "?s1 getLoc:has_Longitude ?long\n" + "}");
		Query query = QueryFactory.create(queryString.toString());
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query);

		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?long");
				if (name != null)
					System.out.println("City -  " + name.toString());
				else
					System.out.println("Not Found!");

			}
		} finally {
			qexec.close();
		}

	}

}

//PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
//PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
//PREFIX getApp: <http://www.semanticweb.org/SER-531/Team-14/Applicants#>
//PREFIX getLoc: <http://www.semanticweb.org/SER-531/Team-14/Location#>
//
//select ?name ?loc ?lat ?long
//WHERE{
//?s getApp:has_Name ?name .
//?s getApp:lives_in ?loc .
//?s getApp:schoolLevel "Bachelor" .
//?s1 getLoc:has_Name ?loc .
//?s1 getLoc:has_Latitude ?lat .
//?s1 getLoc:has_Longitude ?long
//}
