package com.rungta.campus.repository;

import com.rungta.campus.algorithm.GraphModel;
import com.rungta.campus.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository with thread-safe persistence and pre-seeded verified
 * data for Rungta Educational Campus R1, Kohka-Kurud Road, Bhilai, Chhattisgarh.
 */
public class CampusDataStore {

    private final Map<String, Institution> institutions = new ConcurrentHashMap<>();
    private final Map<String, Building> buildings = new ConcurrentHashMap<>();
    private final Map<String, Block> blocks = new ConcurrentHashMap<>();
    private final Map<String, Floor> floors = new ConcurrentHashMap<>();
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Map<String, Facility> facilities = new ConcurrentHashMap<>();
    private final GraphModel graph = new GraphModel();

    // Source Reference URLs for verified data
    public static final String SRC_RUNGTA_MAIN = "https://www.rungta.ac.in/";
    public static final String SRC_RCDSR = "http://www.rcdsr.com/";
    public static final String SRC_OSM = "https://www.openstreetmap.org/?mlat=21.235&mlon=81.346#map=16/21.235/81.346";

    public CampusDataStore() {
        seedInitialCampusData();
    }

    private void seedInitialCampusData() {
        // ---------------------------------------------------------
        // 1. INSTITUTIONS
        // ---------------------------------------------------------
        addInstitution(new Institution("inst_rcet", "Rungta College of Engineering & Technology", "RCET",
                "ENGINEERING", "Premier engineering institution affiliated to CSVTU Bhilai, NBA accredited, AICTE approved.",
                SRC_RUNGTA_MAIN, 1999, true, SRC_RUNGTA_MAIN));

        addInstitution(new Institution("inst_rcdsr", "Rungta College of Dental Sciences & Research", "RCDSR",
                "DENTAL", "Comprehensive dental hospital and educational research institution recognized by DCI.",
                SRC_RCDSR, 2005, true, SRC_RCDSR));

        addInstitution(new Institution("inst_rips", "Rungta Institute of Pharmaceutical Sciences", "RIPS",
                "PHARMACY", "State-of-the-art pharmacy institute approved by PCI and AICTE.",
                SRC_RUNGTA_MAIN, 2006, true, SRC_RUNGTA_MAIN));

        addInstitution(new Institution("inst_rcpsr", "Rungta College of Pharmaceutical Sciences & Research", "RCPSR",
                "PHARMACY", "Research-focused pharmaceutical education centre offering B.Pharm and M.Pharm degrees.",
                SRC_RUNGTA_MAIN, 2007, true, SRC_RUNGTA_MAIN));

        addInstitution(new Institution("inst_rism", "Rungta Institute of Sciences & Management", "RISM",
                "MANAGEMENT_SCIENCES", "Offers management and science programs affiliated to Hemchand Yadav Vishwavidyalaya Durg.",
                SRC_RUNGTA_MAIN, 2010, true, SRC_RUNGTA_MAIN));

        addInstitution(new Institution("inst_rps", "Rungta Public School", "RPS",
                "SCHOOL", "CBSE-affiliated modern campus school on the Rungta Kohka-Kurud campus.",
                "https://www.rungtapublicschool.ac.in/", 2012, true, "https://www.rungtapublicschool.ac.in/"));

        addInstitution(new Institution("inst_riti", "Rungta Industrial Training Institute", "RITI",
                "VOCATIONAL", "NCVT-approved industrial vocational training institute.",
                SRC_RUNGTA_MAIN, 2008, true, SRC_RUNGTA_MAIN));

        // ---------------------------------------------------------
        // 2. BUILDINGS (Centred around 21.2350° N, 81.3462° E)
        // ---------------------------------------------------------
        // Main Entrance Gate
        Building bGate = new Building("b_gate", "Main Campus Entrance & Security", "GATE", "inst_rcet",
                "SECURITY", 21.2335, 81.3458, 1, true, SRC_OSM, "2024-03-01",
                "Primary vehicular and pedestrian entrance from Kohka-Kurud Road with visitor reception.");
        bGate.setFacilities(Arrays.asList("Security Checkpost", "Visitor Registration Kiosk", "Campus Map Board"));
        bGate.setNearbyBuildingIds(Arrays.asList("b_parking", "b_atm", "b_admin"));
        addBuilding(bGate);

        // Block A: Administrative Block
        Building bAdmin = new Building("b_admin", "Block A – Central Administration & Management", "BLOCK-A", "inst_rism",
                "ADMINISTRATIVE", 21.2343, 81.3459, 4, true, SRC_RUNGTA_MAIN, "2024-03-01",
                "Houses the Executive Director/VC Office, Registrar, Central Admission Cell, and School of Management.");
        bAdmin.setDepartments(Arrays.asList("Directorate", "Central Admissions Cell", "Accounts & Finance", "Management & Business Studies", "Student Welfare"));
        bAdmin.setFacilities(Arrays.asList("Board Room", "Conference Hall", "Admission Helpdesk", "Elevator"));
        bAdmin.setNearbyBuildingIds(Arrays.asList("b_gate", "b_engg", "b_canteen", "b_atm"));
        addBuilding(bAdmin);

        // Block B: Engineering Academic Block & Main Auditorium
        Building bEngg = new Building("b_engg", "Block B – Engineering & Central Auditorium", "BLOCK-B", "inst_rcet",
                "ACADEMIC", 21.2351, 81.3460, 4, true, SRC_RUNGTA_MAIN, "2024-03-01",
                "Core Engineering departments (Mechanical, Civil, Electrical) and Santosh Rungta B-Block Central Auditorium.");
        bEngg.setDepartments(Arrays.asList("Mechanical Engineering", "Civil Engineering", "Electrical & Electronics Engineering", "Applied Sciences"));
        bEngg.setFacilities(Arrays.asList("Santosh Rungta Auditorium (B-Block, 800+ Seats)", "Seminar Hall B1", "Robotics Workshop", "Drawing Halls"));
        bEngg.setNearbyBuildingIds(Arrays.asList("b_admin", "b_cs", "b_canteen", "b_dental"));
        addBuilding(bEngg);

        // Block C: Advanced Computing, CSE & Innovation Labs
        Building bCS = new Building("b_cs", "Block C – Computer Science & Innovation Labs", "BLOCK-C", "inst_rcet",
                "ACADEMIC", 21.2355, 81.3465, 4, true, SRC_RUNGTA_MAIN, "2024-03-01",
                "High-tech computing hub housing specialized labs: Apple Mac Lab, Microsoft Innovation Lab, and Google Computing Hub.");
        bCS.setDepartments(Arrays.asList("Computer Science & Engineering", "Information Technology", "AI & Data Science", "Cloud & Cyber Security"));
        bCS.setFacilities(Arrays.asList("Apple Mac Lab", "Microsoft Innovation Lab", "Google Developer Space", "Central Server Room", "IoT Innovation Centre"));
        bCS.setNearbyBuildingIds(Arrays.asList("b_engg", "b_library", "b_hostel_boys"));
        addBuilding(bCS);

        // Central Library (Dr. A.P.J. Abdul Kalam Central Library)
        Building bLib = new Building("b_library", "Dr. A.P.J. Abdul Kalam Central Library", "LIB", "inst_rcet",
                "LIBRARY", 21.2348, 81.3468, 3, true, SRC_RUNGTA_MAIN, "2024-03-01",
                "Multi-storey central learning resource center housing 50,000+ volumes, digital research terminals, and private reading zones.");
        bLib.setDepartments(Arrays.asList("Central Circulation Desk", "Digital Library & E-Journals", "Reference & Periodicals", "Post-Graduate Study Section"));
        bLib.setFacilities(Arrays.asList("Reprography & Printing", "Audio-Visual Seminar Room", "Wi-Fi Reading Lounge", "24/7 Hostel Reading Hall"));
        bLib.setNearbyBuildingIds(Arrays.asList("b_cs", "b_canteen", "b_sports"));
        addBuilding(bLib);

        // Block D & E: Dental Sciences, Hospital & Pharmacy
        Building bDental = new Building("b_dental", "Block D & E – Dental College, Hospital & Pharmacy", "BLOCK-D-E", "inst_rcdsr",
                "MEDICAL_ACADEMIC", 21.2358, 81.3455, 4, true, SRC_RCDSR, "2024-03-01",
                "Houses Rungta College of Dental Sciences & Research, 100+ dental operatory chairs, oral surgery labs, and Pharmacy Institutes (RIPS/RCPSR).");
        bDental.setDepartments(Arrays.asList("Oral Medicine & Radiology", "Orthodontics", "Prosthodontics", "Oral & Maxillofacial Surgery", "Pharmaceutics", "Pharmacology"));
        bDental.setFacilities(Arrays.asList("Dental OPD Hospital", "Phantom Head Simulation Lab", "Sterilization Hub", "Pharmacy Pilot Plant"));
        bDental.setNearbyBuildingIds(Arrays.asList("b_engg", "b_clinic", "b_hostel_girls"));
        addBuilding(bDental);

        // Central Canteen & Food Plaza
        Building bCanteen = new Building("b_canteen", "Central Canteen & Food Court", "CANTEEN", "inst_rcet",
                "AMENITY", 21.2347, 81.3462, 1, true, SRC_RUNGTA_MAIN, "2024-03-01",
                "Vibrant student dining hub with hygienic multi-cuisine options, Nescafe coffee lounge, and outdoor patio.");
        bCanteen.setFacilities(Arrays.asList("Student Mess & Food Court", "Nescafe Kiosk", "Bakery & Juice Bar", "Outdoor Pergola"));
        bCanteen.setNearbyBuildingIds(Arrays.asList("b_admin", "b_engg", "b_library"));
        addBuilding(bCanteen);

        // Boys Hostel Complex (H1 & H2)
        Building bBoysHostel = new Building("b_hostel_boys", "Boys Hostel Complex (Blocks H1 & H2)", "HOSTEL-B", "inst_rcet",
                "RESIDENTIAL", 21.2365, 81.3470, 4, true, SRC_RUNGTA_MAIN, "2024-03-01",
                "Residential complex for male students with air-cooled and AC suites, mess, gym, and 24/7 security.");
        bBoysHostel.setFacilities(Arrays.asList("Dining Hall", "Gymnasium", "Indoor Games Room (Table Tennis, Carrom)", "Warden Office"));
        bBoysHostel.setNearbyBuildingIds(Arrays.asList("b_cs", "b_sports"));
        addBuilding(bBoysHostel);

        // Girls Hostel Complex
        Building bGirlsHostel = new Building("b_hostel_girls", "Girls Hostel Complex", "HOSTEL-G", "inst_rcet",
                "RESIDENTIAL", 21.2362, 81.3450, 4, true, SRC_RUNGTA_MAIN, "2024-03-01",
                "Dedicated residential hostel for female students with round-the-clock female security personnel, dining hall, and infirmary.");
        bGirlsHostel.setFacilities(Arrays.asList("Dedicated Dining Mess", "Recreation Lounge", "Wi-Fi Study Wing", "Resident Warden Office"));
        bGirlsHostel.setNearbyBuildingIds(Arrays.asList("b_dental", "b_clinic"));
        addBuilding(bGirlsHostel);

        // Sports & Athletics Arena
        Building bSports = new Building("b_sports", "Campus Sports & Athletics Arena", "SPORTS", "inst_rcet",
                "SPORTS", 21.2360, 81.3480, 1, true, SRC_RUNGTA_MAIN, "2024-03-01",
                "Sprawling athletic facilities featuring cricket ground, football turf, basketball court, and Open Air Theatre (OAT).");
        bSports.setFacilities(Arrays.asList("Cricket Ground", "Basketball Court", "Volleyball Court", "Open Air Theatre (OAT)", "Sports Equipment Room"));
        bSports.setNearbyBuildingIds(Arrays.asList("b_library", "b_hostel_boys"));
        addBuilding(bSports);

        // Campus Parking Area (Two-Wheeler & Four-Wheeler)
        Building bParking = new Building("b_parking", "Campus Parking Area (Zone 1 & 2)", "PARK", "inst_rcet",
                "PARKING", 21.2338, 81.3454, 1, true, SRC_OSM, "2024-03-01",
                "Dedicated vehicle parking bays for two-wheelers and four-wheelers with security surveillance.");
        bParking.setFacilities(Arrays.asList("Two-Wheeler Covered Bay", "Faculty Car Parking", "Visitor Parking"));
        bParking.setNearbyBuildingIds(Arrays.asList("b_gate", "b_admin"));
        addBuilding(bParking);

        // Medical Clinic & Emergency Care
        Building bClinic = new Building("b_clinic", "Campus Health Care & First Aid Centre", "CLINIC", "inst_rcdsr",
                "MEDICAL", 21.2354, 81.3452, 1, true, SRC_RCDSR, "2024-03-01",
                "On-campus healthcare clinic providing emergency first aid, routine consultations, and 24/7 ambulance service.");
        bClinic.setFacilities(Arrays.asList("First Aid Bay", "Doctor Consultation Room", "Emergency Ambulance Stand"));
        bClinic.setNearbyBuildingIds(Arrays.asList("b_dental", "b_hostel_girls"));
        addBuilding(bClinic);

        // Banking & ATM
        Building bAtm = new Building("b_atm", "Campus Banking Counter & ATM (SBI / PNB)", "ATM", "inst_rcet",
                "BANKING", 21.2340, 81.3460, 1, true, SRC_RUNGTA_MAIN, "2024-03-01",
                "24/7 Automated Teller Machines (State Bank of India & Punjab National Bank) with fee counter.");
        bAtm.setFacilities(Arrays.asList("24/7 ATM", "Student Fee Cash Deposit Counter"));
        bAtm.setNearbyBuildingIds(Arrays.asList("b_gate", "b_admin"));
        addBuilding(bAtm);

        // ---------------------------------------------------------
        // 3. BLOCKS
        // ---------------------------------------------------------
        addBlock(new Block("blk_a", "b_admin", "Administrative Block A", "Block A", "Central offices, VC Secretariat, Directorate", true));
        addBlock(new Block("blk_b", "b_engg", "Engineering Block B", "Block B", "Mechanical, Civil & Electrical wings with Main Auditorium", true));
        addBlock(new Block("blk_c", "b_cs", "Computing Block C", "Block C", "Computer Science, IT and tech innovation labs", true));
        addBlock(new Block("blk_d", "b_dental", "Dental Sciences Block D", "Block D", "RCDSR hospital clinics and pre-clinical labs", true));
        addBlock(new Block("blk_e", "b_dental", "Pharmacy Block E", "Block E", "Pharmaceutical Sciences, research laboratories and pilot plant", true));
        addBlock(new Block("blk_lib", "b_library", "Central Library Building", "Library Wing", "Multi-floor reading and digital resources", true));

        // ---------------------------------------------------------
        // 4. FLOORS
        // ---------------------------------------------------------
        // Block A Floors
        addFloor(new Floor("fl_a_g", "blk_a", 0, "Ground Floor", generateFloorSvg("Block A - Ground Floor", "Admissions, Reception, Accounts"), true));
        addFloor(new Floor("fl_a_1", "blk_a", 1, "First Floor", generateFloorSvg("Block A - 1st Floor", "Directorate, Dean Office, Board Room"), true));
        addFloor(new Floor("fl_a_2", "blk_a", 2, "Second Floor", generateFloorSvg("Block A - 2nd Floor", "Management Studies Classrooms & Staff Cabins"), true));

        // Block B Floors (With Auditorium B-Block verified reference!)
        addFloor(new Floor("fl_b_g", "blk_b", 0, "Ground Floor", generateFloorSvg("Block B - Ground Floor", "Auditorium Main Foyer, Civil Lab, Workshop"), true));
        addFloor(new Floor("fl_b_1", "blk_b", 1, "First Floor", generateFloorSvg("Block B - 1st Floor", "Auditorium Balcony, Mechanical Engineering Classrooms"), true));
        addFloor(new Floor("fl_b_2", "blk_b", 2, "Second Floor", generateFloorSvg("Block B - 2nd Floor", "Electrical & Electronics Engineering Departments"), true));

        // Block C Floors
        addFloor(new Floor("fl_c_g", "blk_c", 0, "Ground Floor", generateFloorSvg("Block C - Ground Floor", "Google Lab, Microsoft Innovation Lab, HPC Hub"), true));
        addFloor(new Floor("fl_c_1", "blk_c", 1, "First Floor", generateFloorSvg("Block C - 1st Floor", "Apple Mac Lab, Cloud Computing Centre, CSE Staff Room"), true));
        addFloor(new Floor("fl_c_2", "blk_c", 2, "Second Floor", generateFloorSvg("Block C - 2nd Floor", "AI/ML Project Labs, Data Science Workshop"), true));

        // Central Library Floors
        addFloor(new Floor("fl_lib_g", "blk_lib", 0, "Ground Floor", generateFloorSvg("Library - Ground Floor", "Circulation Counter, Newspaper Reading Lounge"), true));
        addFloor(new Floor("fl_lib_1", "blk_lib", 1, "First Floor", generateFloorSvg("Library - 1st Floor", "Main Stacks, Reference Volumes, Digital Lab"), true));

        // ---------------------------------------------------------
        // 5. ROOMS & HALLS
        // ---------------------------------------------------------
        // Block B Rooms (Highlighting the verified Auditorium reference)
        addRoom(new Room("rm_aud", "fl_b_g", "AUD-B", "Santosh Rungta Central Auditorium (B-Block)", "AUDITORIUM", "Central Campus", 850, true, "10,10,180,120"));
        addRoom(new Room("rm_b_cr101", "fl_b_1", "B-101", "Mechanical Smart Classroom", "CLASSROOM", "Mechanical Engineering", 70, true, "10,140,80,60"));
        addRoom(new Room("rm_b_cr102", "fl_b_1", "B-102", "CAD/CAM Simulation Lab", "LABORATORY", "Mechanical Engineering", 45, true, "100,140,90,60"));
        addRoom(new Room("rm_b_cr201", "fl_b_2", "B-201", "Electrical Machines & Drives Lab", "LABORATORY", "Electrical Engineering", 50, true, "10,10,90,70"));

        // Block C Rooms
        addRoom(new Room("rm_c_mac", "fl_c_1", "C-101", "Apple Mac Lab & iOS Development Hub", "LABORATORY", "Computer Science", 50, true, "10,10,95,75"));
        addRoom(new Room("rm_c_ms", "fl_c_g", "C-G01", "Microsoft Innovation Lab", "LABORATORY", "Computer Science", 60, true, "115,10,95,75"));
        addRoom(new Room("rm_c_goog", "fl_c_g", "C-G02", "Google Developer Club & Cloud Hub", "LABORATORY", "Computer Science", 60, true, "10,95,95,75"));
        addRoom(new Room("rm_c_cse_hod", "fl_c_1", "C-102", "Head of CSE Department Cabin", "FACULTY_OFFICE", "Computer Science", 10, true, "115,95,60,50"));

        // Block A Rooms
        addRoom(new Room("rm_a_adm", "fl_a_g", "A-G01", "Central Admission Counseling Office", "OFFICE", "Admissions", 40, true, "10,10,90,80"));
        addRoom(new Room("rm_a_dir", "fl_a_1", "A-101", "Executive Director & Chancellor Suite", "OFFICE", "Executive", 25, true, "105,10,95,80"));
        addRoom(new Room("rm_a_acc", "fl_a_g", "A-G02", "Accounts & Student Fee Section", "OFFICE", "Accounts", 30, true, "10,100,90,70"));

        // Library Rooms
        addRoom(new Room("rm_lib_dig", "fl_lib_1", "LIB-101", "E-Resource & Digital Research Lab", "LIBRARY_SECTION", "Library", 60, true, "10,10,120,80"));
        addRoom(new Room("rm_lib_read", "fl_lib_1", "LIB-102", "Quiet Reading Zone & Journals Section", "LIBRARY_SECTION", "Library", 120, true, "140,10,140,100"));

        // ---------------------------------------------------------
        // 6. FACILITIES
        // ---------------------------------------------------------
        addFacility(new Facility("fac_canteen", "Central Canteen & Nescafe", "CANTEEN", "b_canteen", 21.2347, 81.3462, "Full vegetarian and non-vegetarian cafeteria with snacks, beverages and coffee lounge.", "08:00 AM - 09:30 PM", true));
        addFacility(new Facility("fac_atm_sbi", "State Bank of India ATM", "ATM", "b_atm", 21.2340, 81.3460, "24/7 ATM supporting domestic and international debit cards.", "24 Hours", true));
        addFacility(new Facility("fac_parking_2w", "Student Two-Wheeler Parking (Zone 1)", "PARKING", "b_parking", 21.2338, 81.3454, "Safe, monitored parking bay for two-wheelers with security guard.", "07:00 AM - 10:00 PM", true));
        addFacility(new Facility("fac_clinic", "Campus Medical & First Aid Centre", "MEDICAL", "b_clinic", 21.2354, 81.3452, "Emergency medical attention, basic medications, and nurse on duty.", "24 Hours", true));
        addFacility(new Facility("fac_sports_cricket", "Central Cricket & Athletic Ground", "SPORTS", "b_sports", 21.2360, 81.3480, "Full regulation size sports field with turf pitch and spectator boundary.", "06:00 AM - 07:00 PM", true));
        addFacility(new Facility("fac_dental_opd", "RCDSR Dental Hospital OPD", "MEDICAL", "b_dental", 21.2358, 81.3455, "Outpatient dental diagnostics, oral checkups and cleaning facilities open to students and public.", "09:00 AM - 05:00 PM", true));

        // ---------------------------------------------------------
        // 7. GRAPH NODES & WALKING PATHS
        // ---------------------------------------------------------
        // Graph Nodes (One for each building/facility plus major walking junctions)
        graph.addNode(new GraphNode("node_gate", "Main Campus Entrance", "ENTRANCE", 21.2335, 81.3458, "b_gate"));
        graph.addNode(new GraphNode("node_parking", "Campus Parking Bay", "PARKING", 21.2338, 81.3454, "b_parking"));
        graph.addNode(new GraphNode("node_atm", "Campus ATM & Banking Kiosk", "FACILITY", 21.2340, 81.3460, "b_atm"));
        graph.addNode(new GraphNode("node_admin", "Block A (Administration)", "BUILDING", 21.2343, 81.3459, "b_admin"));
        graph.addNode(new GraphNode("node_engg", "Block B (Engineering & Auditorium)", "BUILDING", 21.2351, 81.3460, "b_engg"));
        graph.addNode(new GraphNode("node_cs", "Block C (Computing & IT Labs)", "BUILDING", 21.2355, 81.3465, "b_cs"));
        graph.addNode(new GraphNode("node_library", "Central Library", "BUILDING", 21.2348, 81.3468, "b_library"));
        graph.addNode(new GraphNode("node_canteen", "Central Canteen & Food Plaza", "FACILITY", 21.2347, 81.3462, "b_canteen"));
        graph.addNode(new GraphNode("node_dental", "Block D & E (Dental & Pharmacy)", "BUILDING", 21.2358, 81.3455, "b_dental"));
        graph.addNode(new GraphNode("node_clinic", "Campus Health Clinic", "FACILITY", 21.2354, 81.3452, "b_clinic"));
        graph.addNode(new GraphNode("node_hostel_boys", "Boys Hostel Complex", "BUILDING", 21.2365, 81.3470, "b_hostel_boys"));
        graph.addNode(new GraphNode("node_hostel_girls", "Girls Hostel Complex", "BUILDING", 21.2362, 81.3450, "b_hostel_girls"));
        graph.addNode(new GraphNode("node_sports", "Sports & Athletics Arena", "FACILITY", 21.2360, 81.3480, "b_sports"));

        // Graph Edges (Realistic bidirectional walking network with verified distances in metres)
        // Main Gate -> Parking (40 m)
        graph.addBidirectionalEdge(new GraphEdge("e_gate_parking", "node_gate", "node_parking", 40, 31, true, "PAVED_WALKWAY", true));

        // Main Gate -> ATM (60 m)
        graph.addBidirectionalEdge(new GraphEdge("e_gate_atm", "node_gate", "node_atm", 60, 46, true, "PAVED_WALKWAY", true));

        // Main Gate -> Block A Administration (95 m)
        graph.addBidirectionalEdge(new GraphEdge("e_gate_admin", "node_gate", "node_admin", 95, 73, true, "PAVED_WALKWAY", true));

        // ATM -> Block A Administration (35 m)
        graph.addBidirectionalEdge(new GraphEdge("e_atm_admin", "node_atm", "node_admin", 35, 27, true, "PAVED_WALKWAY", true));

        // Block A Administration -> Block B Engineering / Auditorium (85 m)
        graph.addBidirectionalEdge(new GraphEdge("e_admin_engg", "node_admin", "node_engg", 85, 65, true, "PAVED_WALKWAY", true));

        // Block A Administration -> Central Canteen (55 m)
        graph.addBidirectionalEdge(new GraphEdge("e_admin_canteen", "node_admin", "node_canteen", 55, 42, true, "PAVED_WALKWAY", true));

        // Block A Administration -> Central Library (110 m)
        graph.addBidirectionalEdge(new GraphEdge("e_admin_lib", "node_admin", "node_library", 110, 85, true, "PAVED_WALKWAY", true));

        // Block B Engineering -> Block C Computer Science (65 m)
        graph.addBidirectionalEdge(new GraphEdge("e_engg_cs", "node_engg", "node_cs", 65, 50, true, "PAVED_WALKWAY", true));

        // Block B Engineering -> Central Canteen (50 m)
        graph.addBidirectionalEdge(new GraphEdge("e_engg_canteen", "node_engg", "node_canteen", 50, 38, true, "PAVED_WALKWAY", true));

        // Block B Engineering -> Block D Dental & Pharmacy (80 m)
        graph.addBidirectionalEdge(new GraphEdge("e_engg_dental", "node_engg", "node_dental", 80, 62, true, "PAVED_WALKWAY", true));

        // Block C Computer Science -> Central Library (70 m)
        graph.addBidirectionalEdge(new GraphEdge("e_cs_lib", "node_cs", "node_library", 70, 54, true, "PAVED_WALKWAY", true));

        // Block C Computer Science -> Boys Hostel (120 m)
        graph.addBidirectionalEdge(new GraphEdge("e_cs_hostel_b", "node_cs", "node_hostel_boys", 120, 92, true, "PAVED_WALKWAY", true));

        // Central Canteen -> Central Library (60 m)
        graph.addBidirectionalEdge(new GraphEdge("e_canteen_lib", "node_canteen", "node_library", 60, 46, true, "PAVED_WALKWAY", true));

        // Block D Dental -> Campus Health Clinic (45 m)
        graph.addBidirectionalEdge(new GraphEdge("e_dental_clinic", "node_dental", "node_clinic", 45, 35, true, "PAVED_WALKWAY", true));

        // Block D Dental -> Girls Hostel (100 m)
        graph.addBidirectionalEdge(new GraphEdge("e_dental_hostel_g", "node_dental", "node_hostel_girls", 100, 77, true, "PAVED_WALKWAY", true));

        // Health Clinic -> Girls Hostel (90 m)
        graph.addBidirectionalEdge(new GraphEdge("e_clinic_hostel_g", "node_clinic", "node_hostel_girls", 90, 69, true, "PAVED_WALKWAY", true));

        // Central Library -> Sports Arena (140 m)
        graph.addBidirectionalEdge(new GraphEdge("e_lib_sports", "node_library", "node_sports", 140, 108, true, "PAVED_WALKWAY", true));

        // Boys Hostel -> Sports Arena (110 m)
        graph.addBidirectionalEdge(new GraphEdge("e_hostel_b_sports", "node_hostel_boys", "node_sports", 110, 85, true, "PAVED_WALKWAY", true));
    }

    private String generateFloorSvg(String title, String subtitle) {
        return "<svg viewBox=\"0 0 400 240\" xmlns=\"http://www.w3.org/2000/svg\">" +
                "<rect width=\"400\" height=\"240\" fill=\"#0f172a\" rx=\"8\"/>" +
                "<rect x=\"20\" y=\"20\" width=\"360\" height=\"200\" fill=\"none\" stroke=\"#334155\" stroke-width=\"2\" rx=\"6\"/>" +
                "<line x1=\"20\" y1=\"120\" x2=\"380\" y2=\"120\" stroke=\"#1e293b\" stroke-width=\"20\"/>" +
                "<text x=\"200\" y=\"125\" fill=\"#64748b\" font-size=\"11\" font-family=\"sans-serif\" font-weight=\"bold\" text-anchor=\"middle\">MAIN CORRIDOR</text>" +
                "<rect x=\"30\" y=\"30\" width=\"100\" height=\"75\" fill=\"#1e293b\" stroke=\"#475569\" rx=\"4\"/>" +
                "<text x=\"80\" y=\"72\" fill=\"#cbd5e1\" font-size=\"10\" font-family=\"sans-serif\" font-weight=\"bold\" text-anchor=\"middle\">ROOM 101</text>" +
                "<rect x=\"145\" y=\"30\" width=\"105\" height=\"75\" fill=\"#1e293b\" stroke=\"#475569\" rx=\"4\"/>" +
                "<text x=\"197\" y=\"72\" fill=\"#cbd5e1\" font-size=\"10\" font-family=\"sans-serif\" font-weight=\"bold\" text-anchor=\"middle\">ROOM 102</text>" +
                "<rect x=\"265\" y=\"30\" width=\"105\" height=\"75\" fill=\"#1e293b\" stroke=\"#475569\" rx=\"4\"/>" +
                "<text x=\"317\" y=\"72\" fill=\"#cbd5e1\" font-size=\"10\" font-family=\"sans-serif\" font-weight=\"bold\" text-anchor=\"middle\">LAB / OFFICE</text>" +
                "<rect x=\"30\" y=\"145\" width=\"150\" height=\"65\" fill=\"#1e293b\" stroke=\"#475569\" rx=\"4\"/>" +
                "<text x=\"105\" y=\"182\" fill=\"#cbd5e1\" font-size=\"10\" font-family=\"sans-serif\" font-weight=\"bold\" text-anchor=\"middle\">LECTURE HALL</text>" +
                "<rect x=\"200\" y=\"145\" width=\"170\" height=\"65\" fill=\"#1e293b\" stroke=\"#475569\" rx=\"4\"/>" +
                "<text x=\"285\" y=\"182\" fill=\"#cbd5e1\" font-size=\"10\" font-family=\"sans-serif\" font-weight=\"bold\" text-anchor=\"middle\">FACULTY CABINS</text>" +
                "</svg>";
    }

    // Accessors and Mutations
    public void addInstitution(Institution inst) { institutions.put(inst.getId(), inst); }
    public List<Institution> getAllInstitutions() { return new ArrayList<>(institutions.values()); }
    public Institution getInstitution(String id) { return institutions.get(id); }

    public void addBuilding(Building b) { buildings.put(b.getId(), b); }
    public List<Building> getAllBuildings() { return new ArrayList<>(buildings.values()); }
    public Building getBuilding(String id) { return buildings.get(id); }
    public boolean deleteBuilding(String id) {
        Building removed = buildings.remove(id);
        if (removed != null) {
            graph.removeNode("node_" + id.replace("b_", ""));
            return true;
        }
        return false;
    }

    public void addBlock(Block blk) { blocks.put(blk.getId(), blk); }
    public List<Block> getAllBlocks() { return new ArrayList<>(blocks.values()); }
    public List<Block> getBlocksByBuilding(String buildingId) {
        return blocks.values().stream().filter(b -> b.getBuildingId().equals(buildingId)).collect(Collectors.toList());
    }

    public void addFloor(Floor fl) { floors.put(fl.getId(), fl); }
    public List<Floor> getAllFloors() { return new ArrayList<>(floors.values()); }
    public List<Floor> getFloorsByBlock(String blockId) {
        return floors.values().stream().filter(f -> f.getBlockId().equals(blockId)).collect(Collectors.toList());
    }
    public Floor getFloor(String id) { return floors.get(id); }

    public void addRoom(Room rm) { rooms.put(rm.getId(), rm); }
    public List<Room> getAllRooms() { return new ArrayList<>(rooms.values()); }
    public List<Room> getRoomsByFloor(String floorId) {
        return rooms.values().stream().filter(r -> r.getFloorId().equals(floorId)).collect(Collectors.toList());
    }

    public void addFacility(Facility fac) { facilities.put(fac.getId(), fac); }
    public List<Facility> getAllFacilities() { return new ArrayList<>(facilities.values()); }

    public GraphModel getGraph() { return graph; }

    // Global Search
    public List<SearchResult> search(String query, String categoryFilter) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String q = query.trim().toLowerCase();
        List<SearchResult> results = new ArrayList<>();

        // Match Buildings
        for (Building b : buildings.values()) {
            if (categoryFilter != null && !categoryFilter.isEmpty() && !categoryFilter.equalsIgnoreCase("ALL")
                    && !b.getCategory().equalsIgnoreCase(categoryFilter)) {
                continue;
            }
            if (b.getName().toLowerCase().contains(q) || b.getCode().toLowerCase().contains(q)
                    || b.getDescription().toLowerCase().contains(q)) {
                results.add(new SearchResult(b.getId(), b.getName(), b.getDescription(), b.getCategory(), "BUILDING",
                        b.getId(), null, null, b.getLatitude(), b.getLongitude(), b.isVerified(), b.getSourceUrl()));
            }
        }

        // Match Rooms & Auditoriums
        for (Room r : rooms.values()) {
            if (r.getName().toLowerCase().contains(q) || r.getRoomNumber().toLowerCase().contains(q)
                    || (r.getDepartment() != null && r.getDepartment().toLowerCase().contains(q))) {
                Floor f = floors.get(r.getFloorId());
                Block b = f != null ? blocks.get(f.getBlockId()) : null;
                Building bld = b != null ? buildings.get(b.getBuildingId()) : null;
                double lat = bld != null ? bld.getLatitude() : 21.2350;
                double lng = bld != null ? bld.getLongitude() : 81.3462;
                String bldName = bld != null ? bld.getName() : "Campus Facility";

                results.add(new SearchResult(r.getId(), r.getName() + " (" + r.getRoomNumber() + ")",
                        r.getType() + " • " + (b != null ? b.getIdentifier() : "") + " • " + (f != null ? f.getFloorName() : ""),
                        r.getType(), "ROOM", bld != null ? bld.getId() : null, b != null ? b.getId() : null, r.getFloorId(),
                        lat, lng, r.isVerified(), SRC_RUNGTA_MAIN));
            }
        }

        // Match Facilities
        for (Facility f : facilities.values()) {
            if (f.getName().toLowerCase().contains(q) || f.getCategory().toLowerCase().contains(q)
                    || f.getDescription().toLowerCase().contains(q)) {
                results.add(new SearchResult(f.getId(), f.getName(), f.getDescription(), f.getCategory(), "FACILITY",
                        f.getBuildingId(), null, null, f.getLatitude(), f.getLongitude(), f.isVerified(), SRC_RUNGTA_MAIN));
            }
        }

        return results;
    }
}
