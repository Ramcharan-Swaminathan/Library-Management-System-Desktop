DROP TABLE Library CASCADE CONSTRAINTS;
DROP TABLE admin CASCADE CONSTRAINTS;
DROP TABLE LibraryStaff CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;
DROP TABLE Book CASCADE CONSTRAINTS;
DROP TABLE Publisher CASCADE CONSTRAINTS;
DROP TABLE Author CASCADE CONSTRAINTS;
DROP TABLE AuthorWithBook CASCADE CONSTRAINTS;
DROP TABLE BorrowedBook CASCADE CONSTRAINTS;
DROP TABLE reservation CASCADE CONSTRAINTS;
DROP TABLE CartCollection CASCADE CONSTRAINTS;
DROP TABLE RequestBook CASCADE CONSTRAINTS;
DROP TABLE RequestBookWithDetails CASCADE CONSTRAINTS;
DROP TABLE Rating CASCADE CONSTRAINTS;
DROP TABLE Feedback CASCADE CONSTRAINTS;
DROP TABLE HelpdeskRequests CASCADE CONSTRAINTS;



CREATE TABLE Library (
    LibraryName VARCHAR2(100) PRIMARY KEY,
    Location VARCHAR2(255) NOT NULL,
    ContactInfo VARCHAR2(100) NOT NULL,
    CONSTRAINT chk_Library_ContactInfo 
        CHECK (REGEXP_LIKE(ContactInfo, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'))
);




CREATE TABLE Admin (
    AdminID CHAR(7) PRIMARY KEY,
    Password VARCHAR2(30) NOT NULL,
    AdminName VARCHAR2(100),
    Email VARCHAR2(100) NOT NULL UNIQUE,
    ContactInfo VARCHAR2(20) NOT NULL,
    LibraryName VARCHAR2(100),
    CONSTRAINT chk_AdminID 
        CHECK (REGEXP_LIKE(AdminID, '^\d{7}$')),
    CONSTRAINT chk_Admin_Email 
        CHECK (REGEXP_LIKE(Email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')),
    CONSTRAINT fk_Admin_Library 
        FOREIGN KEY (LibraryName) REFERENCES Library(LibraryName)
);



CREATE TABLE LibraryStaff (
    StaffID CHAR(7) PRIMARY KEY,
    Password VARCHAR2(30) NOT NULL,
    StaffName VARCHAR2(100) NOT NULL,
    Role VARCHAR2(50) NOT NULL,
    Email VARCHAR2(100) NOT NULL UNIQUE,
    ContactInfo VARCHAR2(20) NOT NULL,
    LibraryName VARCHAR2(100),
    SupervisorID CHAR(7),
    CONSTRAINT chk_StaffID 
        CHECK (REGEXP_LIKE(StaffID, '^\d{7}$')),
    CONSTRAINT chk_Staff_Email 
        CHECK (REGEXP_LIKE(Email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')),
    CONSTRAINT fk_Staff_Library 
        FOREIGN KEY (LibraryName) REFERENCES Library(LibraryName),
    CONSTRAINT fk_Supervisor_Admin 
        FOREIGN KEY (SupervisorID) REFERENCES Admin(AdminID)
);



CREATE TABLE Users (
    UserID VARCHAR2(20) PRIMARY KEY,
    Password VARCHAR2(30) NOT NULL,
    MembershipType VARCHAR2(10),
    UserName VARCHAR2(100),
    LibraryName VARCHAR2(100),
    Department VARCHAR2(50),
    JoinedYear NUMBER(4),
    Email VARCHAR2(100) NOT NULL UNIQUE,
    NoBorrowedBooks NUMBER DEFAULT 0,
    NoReservedBooks NUMBER DEFAULT 0,
    Fine NUMBER DEFAULT 0,
    Status VARCHAR2(20) DEFAULT 'not blocked',
    CONSTRAINT chk_User_Email 
        CHECK (REGEXP_LIKE(Email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')),
    CHECK (MembershipType IN ('faculty', 'student')),
    CONSTRAINT fk_User_Library 
        FOREIGN KEY (LibraryName) REFERENCES Library(LibraryName)
);



CREATE TABLE Publisher (
    PublisherID VARCHAR2(20) PRIMARY KEY,
    PublisherName VARCHAR2(100),
    Email VARCHAR2(100) NOT NULL UNIQUE,
    ContactInfo VARCHAR2(20) NOT NULL,
    CONSTRAINT chk_Publisher_Email 
        CHECK (REGEXP_LIKE(Email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'))
);







CREATE TABLE Book (
    ISBN VARCHAR2(13) PRIMARY KEY,
    Title VARCHAR2(200),
    PublisherID VARCHAR2(20),
    LibraryName VARCHAR2(100),
    BookGroup VARCHAR2(50),
    TotalCopies NUMBER DEFAULT 0 NOT NULL,
    AvailableCopies NUMBER DEFAULT 0 NOT NULL,
    NoReservation NUMBER DEFAULT 0 NOT NULL,
    RowNo VARCHAR2(10) NOT NULL,
    RackNo VARCHAR2(10) NOT NULL,
    Avg_Rating NUMBER,
    CONSTRAINT chk_ISBN_Format 
        CHECK (REGEXP_LIKE(ISBN, '^97[89][0-9]{10}$')),
    CONSTRAINT chk_RowNo_Format 
        CHECK (REGEXP_LIKE(RowNo, '^Row [0-9]+$')),
    CONSTRAINT chk_RackNo_Format 
        CHECK (REGEXP_LIKE(RackNo, '^Rack [A-Z]$')),
    CONSTRAINT chk_Avg_Rating 
        CHECK (Avg_Rating BETWEEN 0 AND 5),
    CONSTRAINT fk_Book_Publisher 
        FOREIGN KEY (PublisherID) REFERENCES Publisher(PublisherID),
    CONSTRAINT fk_Book_Library 
        FOREIGN KEY (LibraryName) REFERENCES Library(LibraryName)
);




CREATE TABLE Author (
    AuthorID VARCHAR2(20) PRIMARY KEY,
    AuthorName VARCHAR2(100),
    AuthorStream VARCHAR2(50),
    AuthorQualification VARCHAR2(100) NOT NULL,
    Nationality VARCHAR2(50) NOT NULL,
    Email VARCHAR2(100) NOT NULL UNIQUE,
    CONSTRAINT chk_Author_Email 
        CHECK (REGEXP_LIKE(Email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'))
);



CREATE TABLE AuthorWithBook (
    BookID VARCHAR2(20),
    AuthorID VARCHAR2(20),
    PRIMARY KEY (BookID, AuthorID),
    CONSTRAINT fk_AuthorWithBook_Book 
        FOREIGN KEY (BookID) REFERENCES Book(ISBN),
    CONSTRAINT fk_AuthorWithBook_Author 
        FOREIGN KEY (AuthorID) REFERENCES Author(AuthorID)
);




CREATE TABLE BorrowedBook (
    BorrowedID CHAR(10) PRIMARY KEY,
    UserID VARCHAR2(20),
    BookID VARCHAR2(20),
    IssueDate DATE DEFAULT SYSDATE,
    RenewalDate DATE GENERATED ALWAYS AS (IssueDate + 7) VIRTUAL,
    ReturnDate DATE GENERATED ALWAYS AS (IssueDate + 28) VIRTUAL,
    TotalRenewals NUMBER DEFAULT 3,
    RenewalsLeft NUMBER DEFAULT 3,
    Fine NUMBER DEFAULT 0,
    Status VARCHAR2(20) NOT NULL,
    CONSTRAINT chk_BorrowedID 
        CHECK (REGEXP_LIKE(BorrowedID, '^\d{10}$')),
    CONSTRAINT chk_Renewals 
        CHECK (RenewalsLeft BETWEEN 0 AND 3),
    CONSTRAINT chk_TotalRenewals 
        CHECK (TotalRenewals BETWEEN 0 AND 3),
    CONSTRAINT chk_Status 
        CHECK (Status IN ('Borrowed', 'Returned', 'Overdue')),
    CONSTRAINT fk_Borrowed_User 
        FOREIGN KEY (UserID) REFERENCES Users(UserID),
    CONSTRAINT fk_Borrowed_Book 
        FOREIGN KEY (BookID) REFERENCES Book(ISBN)
);


CREATE TABLE Reservation (
    ReservationID VARCHAR2(20) PRIMARY KEY,
    UserID VARCHAR2(20),
    BookID VARCHAR2(20),
    ReservationDate DATE,
    Deadline DATE,
    Status VARCHAR2(20) NOT NULL,
    CONSTRAINT fk_Reservation_User 
        FOREIGN KEY (UserID) REFERENCES Users(UserID),
    CONSTRAINT fk_Reservation_Book 
        FOREIGN KEY (BookID) REFERENCES Book(ISBN)
);




CREATE TABLE CartCollection (
    UserID VARCHAR2(20),
    BookID VARCHAR2(20),
    AddedDate DATE,
    ExpireDate DATE,
    Status VARCHAR2(20) NOT NULL,
    PRIMARY KEY (UserID, BookID),
    CONSTRAINT fk_Cart_User 
        FOREIGN KEY (UserID) REFERENCES Users(UserID),
    CONSTRAINT fk_Cart_Book 
        FOREIGN KEY (BookID) REFERENCES Book(ISBN)
);




CREATE TABLE RequestBook (
    BookID VARCHAR2(20),
    RequestedDate DATE,
    RequestType VARCHAR2(50),
    ResponseDate DATE,
    RequestStatus VARCHAR2(20),
    PRIMARY KEY (BookID, RequestedDate),
    CONSTRAINT chk_RequestStatus 
        CHECK (RequestStatus IN ('Pending', 'Approved')),
    CONSTRAINT fk_RequestBook_Book 
        FOREIGN KEY (BookID) REFERENCES Book(ISBN)
);



CREATE TABLE RequestBookWithDetails (
    UserID VARCHAR2(20),
    RequestDate DATE,
    BookID VARCHAR2(20),
    Title VARCHAR2(200),
    Author VARCHAR2(100),
    PRIMARY KEY (UserID, RequestDate, BookID),
    CONSTRAINT fk_RequestDetails_User 
        FOREIGN KEY (UserID) REFERENCES Users(UserID),
    CONSTRAINT fk_RequestDetails_Book 
        FOREIGN KEY (BookID) REFERENCES Book(ISBN)
);




CREATE TABLE Rating (
    UserID VARCHAR2(20),
    BookID VARCHAR2(20),
    RatingValue NUMBER,
    RatingDate DATE,
    PRIMARY KEY (UserID, BookID),
    CHECK (RatingValue BETWEEN 1 AND 5),
    CONSTRAINT fk_Rating_User 
        FOREIGN KEY (UserID) REFERENCES Users(UserID),
    CONSTRAINT fk_Rating_Book 
        FOREIGN KEY (BookID) REFERENCES Book(ISBN)
);



CREATE TABLE Feedback (
    UserID VARCHAR2(20),
    DateOfComment DATE,
    Comments VARCHAR2(1000),
    Ratings NUMBER,
    Response_Date DATE,
    PRIMARY KEY (UserID, DateOfComment),
    CHECK (Ratings BETWEEN 1 AND 5),
    CONSTRAINT fk_Feedback_User 
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
);


CREATE TABLE HelpdeskRequests (
    RequestID NUMBER GENERATED ALWAYS AS IDENTITY,
    UserID VARCHAR2(20),
    RequestDate DATE,
    IssueType VARCHAR2(50),
    Description VARCHAR2(1000),
    Status VARCHAR2(20) DEFAULT 'Pending',
    Response VARCHAR2(1000),
    ResponseDate DATE,
    PRIMARY KEY (RequestID),
    CONSTRAINT fk_Helpdesk_User 
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

INSERT INTO Library (LibraryName, Location, ContactInfo) 
VALUES('Central Library', 'Main Campus, Block A', 'central@university.edu');
INSERT INTO Library (LibraryName, Location, ContactInfo) 
VALUES('Engineering Library', 'Engineering Block, Floor 2', 'englib@university.edu');
INSERT INTO Library (LibraryName, Location, ContactInfo) 
VALUES('Medical Library', 'Medical Campus, Building B', 'medlib@university.edu');
INSERT INTO Library (LibraryName, Location, ContactInfo) 
VALUES('Science Library', 'Science Wing, Block C', 'scilib@university.edu');
INSERT INTO Library (LibraryName, Location, ContactInfo) 
VALUES('Law Library', 'Law Campus, Ground Floor', 'lawlib@university.edu');

-- Admin Table
INSERT INTO Admin (AdminId, Password, AdminName, Email, ContactInfo, LibraryName) VALUES('1000001', 'pass@1234', 'Alice Johnson', 'alice.j@university.edu', '1234567890', 'Central Library');
INSERT INTO Admin (AdminId, Password, AdminName, Email, ContactInfo, LibraryName) VALUES('1000002', 'securePass1', 'Bob Smith', 'bob.smith@university.edu', '9876543210', 'Engineering Library');
INSERT INTO Admin (AdminId, Password, AdminName, Email, ContactInfo, LibraryName) VALUES('1000003', 'adminPass12', 'Carol White', 'carol.white@university.edu', '5556667777', 'Medical Library');
INSERT INTO Admin (AdminId, Password, AdminName, Email, ContactInfo, LibraryName) VALUES('1000004', 'myPassword!', 'David Black', 'david.black@university.edu', '4443332222', 'Science Library');
INSERT INTO Admin (AdminId, Password, AdminName, Email, ContactInfo, LibraryName) VALUES('1000005', 'admin!2023', 'Eve Grey', 'eve.grey@university.edu', '1112223333', 'Law Library');

INSERT INTO LibraryStaff (StaffId, Password, StaffName, Role, Email, ContactInfo, LibraryName, SupervisorID) 
VALUES('2000001', 'staffPass1', 'Tom Hanks', 'Assistant', 'tom.h@university.edu', '9991112222', 'Central Library', '1000001');

INSERT INTO LibraryStaff (StaffId, Password, StaffName, Role, Email, ContactInfo, LibraryName, SupervisorID) 
VALUES('2000002', 'secure456', 'Emma Stone', 'Technician', 'emma.s@university.edu', '8882223333', 'Engineering Library', '1000002');

INSERT INTO LibraryStaff (StaffId, Password, StaffName, Role, Email, ContactInfo, LibraryName, SupervisorID) 
VALUES('2000003', 'lib@work', 'John Doe', 'Clerk', 'john.d@university.edu', '7773334444', 'Medical Library', '1000003');

INSERT INTO LibraryStaff (StaffId, Password, StaffName, Role, Email, ContactInfo, LibraryName, SupervisorID) 
VALUES('2000004', 'pass2023', 'Lucy Hale', 'IT Support', 'lucy.h@university.edu', '6664445555', 'Science Library', '1000004');

INSERT INTO LibraryStaff (StaffId, Password, StaffName, Role, Email, ContactInfo, LibraryName, SupervisorID) 
VALUES('2000005', 'libTech1', 'Mark Lee', 'Technician', 'mark.l@university.edu', '5556667777', 'Law Library', '1000005');



INSERT INTO Users (UserId, Password, MembershipType, Username, LibraryName, Department, JoinedYear, Email, NoBorrowedBooks, NoReservedBooks, Fine, Status) 
VALUES('U1001', 'userpass1', 'student', 'Ravi Kumar', 'Central Library', 'CSE', 2021, 'ravi.k@university.edu', 0, 0, 0, 'not blocked');

INSERT INTO Users (UserId, Password, MembershipType, Username, LibraryName, Department, JoinedYear, Email, NoBorrowedBooks, NoReservedBooks, Fine, Status) 
VALUES('U1002', 'userpass1', 'faculty', 'Anita Desai', 'Engineering Library', 'IT', 2019, 'anita.d@university.edu', 0, 0, 0, 'not blocked');

INSERT INTO Users (UserId, Password, MembershipType, Username, LibraryName, Department, JoinedYear, Email, NoBorrowedBooks, NoReservedBooks, Fine, Status) 
VALUES('U1003', 'userpass1', 'student', 'Suresh Raina', 'Medical Library', 'ECE', 2022, 'suresh.r@university.edu', 0, 0, 0, 'not blocked');

INSERT INTO Users (UserId, Password, MembershipType, Username, LibraryName, Department, JoinedYear, Email, NoBorrowedBooks, NoReservedBooks, Fine, Status) 
VALUES('U1004', 'userpass1', 'faculty', 'Meera Singh', 'Science Library', 'EEE', 2020, 'meera.s@university.edu', 0, 0, 0, 'not blocked');

INSERT INTO Users (UserId, Password, MembershipType, Username, LibraryName, Department, JoinedYear, Email, NoBorrowedBooks, NoReservedBooks, Fine, Status) 
VALUES('U1005', 'userpass1', 'student', 'Rahul Dravid', 'Law Library', 'CSE', 2023, 'rahul.d@university.edu', 0, 0, 0, 'not blocked');

INSERT INTO Publisher (PublisherId, PublisherName, Email, ContactInfo) VALUES 
('P001', 'Pearson Education', 'contact@pearson.com', '1800123456');

INSERT INTO Publisher (PublisherId, PublisherName, Email, ContactInfo) VALUES 
('P002', 'McGraw Hill', 'info@mcgrawhill.com', '1800987654');

INSERT INTO Publisher (PublisherId, PublisherName, Email, ContactInfo) VALUES 
('P003', 'OReilly Media', 'support@oreilly.com', '1800456123');

INSERT INTO Publisher (PublisherId, PublisherName, Email, ContactInfo) VALUES 
('P004', 'Elsevier', 'sales@elsevier.com', '1800789345');

INSERT INTO Publisher (PublisherId, PublisherName, Email, ContactInfo) VALUES 
('P005', 'Oxford University Press', 'oxford@oup.com', '1800999888');



INSERT INTO Book (ISBN, Title, PublisherId, LibraryName, BookGroup, TotalCopies, AvailableCopies, NoReservation, RowNo, RackNo, Avg_Rating) VALUES 
('9781234567890', 'Computer Networks', 'P001', 'Central Library', 'CSE', 10, 7, 3, 'Row 1', 'Rack A', 4.3);

INSERT INTO Book (ISBN, Title, PublisherId, LibraryName, BookGroup, TotalCopies, AvailableCopies, NoReservation, RowNo, RackNo, Avg_Rating) VALUES 
('9780987654321', 'Human Anatomy', 'P004', 'Medical Library', 'Biology', 12, 12, 0, 'Row 2', 'Rack B', 4.0);

INSERT INTO Book (ISBN, Title, PublisherId, LibraryName, BookGroup, TotalCopies, AvailableCopies, NoReservation, RowNo, RackNo, Avg_Rating) VALUES 
('9791111111111', 'Digital Signal Processing', 'P002', 'Engineering Library', 'ECE', 8, 4, 3, 'Row 3', 'Rack C', 3.9);

INSERT INTO Book (ISBN, Title, PublisherId, LibraryName, BookGroup, TotalCopies, AvailableCopies, NoReservation, RowNo, RackNo, Avg_Rating) VALUES 
('9782222222222', 'Legal Ethics', 'P005', 'Law Library', 'Law', 6, 6, 0, 'Row 4', 'Rack D', 4.1);

INSERT INTO Book (ISBN, Title, PublisherId, LibraryName, BookGroup, TotalCopies, AvailableCopies, NoReservation, RowNo, RackNo, Avg_Rating) VALUES 
('9793333333333', 'Artificial Intelligence', 'P003', 'Science Library', 'CSE', 15, 14, 1, 'Row 5', 'Rack E', 4.7);



INSERT INTO Author (AuthorId, AuthorName, AuthorStream, AuthorQualification, Nationality, Email) VALUES 
('A001', 'Andrew Tanenbaum', 'CSE', 'PhD in Computer Science', 'USA', 'tanenbaum@cs.com');

INSERT INTO Author (AuthorId, AuthorName, AuthorStream, AuthorQualification, Nationality, Email) VALUES 
('A002', 'R.S. Agarwal', 'Mathematics', 'MSc Mathematics', 'India', 'agarwal.math@edu.in');

INSERT INTO Author (AuthorId, AuthorName, AuthorStream, AuthorQualification, Nationality, Email) VALUES 
('A003', 'K. L. Gopal', 'Law', 'LLM', 'India', 'gopal.law@bar.com');

INSERT INTO Author (AuthorId, AuthorName, AuthorStream, AuthorQualification, Nationality, Email) VALUES 
('A004', 'Elaine Marieb', 'Biology', 'PhD in Anatomy', 'USA', 'elaine.bio@univ.com');

INSERT INTO Author (AuthorId, AuthorName, AuthorStream, AuthorQualification, Nationality, Email) VALUES 
('A005', 'Stuart Russell', 'CSE', 'PhD in AI', 'UK', 'stuart.ai@oxford.edu');


INSERT INTO AuthorWithBook (BookId, AuthorId) VALUES ('9780987654321', 'A002');
INSERT INTO AuthorWithBook (BookId, AuthorId) VALUES ('9781234567890', 'A001');
INSERT INTO AuthorWithBook (BookId, AuthorId) VALUES ('9782222222222', 'A004');
INSERT INTO AuthorWithBook (BookId, AuthorId) VALUES ('9791111111111', 'A003');
INSERT INTO AuthorWithBook (BookId, AuthorId) VALUES ('9793333333333', 'A005');


INSERT INTO BorrowedBook (BorrowedID, UserID, BookID, IssueDate, TotalRenewals, RenewalsLeft, Fine, Status)
VALUES ('1000000001', 'U1001', '9781234567890', DATE '2025-04-08', 3, 1, 0, 'Borrowed');

INSERT INTO BorrowedBook (BorrowedID, UserID, BookID, IssueDate, TotalRenewals, RenewalsLeft, Fine, Status)
VALUES ('1000000002', 'U1002', '9780987654321', DATE '2025-04-07', 3, 2, 0, 'Borrowed');

INSERT INTO BorrowedBook (BorrowedID, UserID, BookID, IssueDate, TotalRenewals, RenewalsLeft, Fine, Status)
VALUES ('1000000003', 'U1003', '9791111111111', DATE '2025-04-01', 3, 1, 0, 'Borrowed');

INSERT INTO BorrowedBook (BorrowedID, UserID, BookID, IssueDate, TotalRenewals, RenewalsLeft, Fine, Status)
VALUES ('1000000004', 'U1004', '9782222222222', DATE '2025-04-01', 3, 0, 0, 'Borrowed');

INSERT INTO BorrowedBook (BorrowedID, UserID, BookID, IssueDate, TotalRenewals, RenewalsLeft, Fine, Status)
VALUES ('1000000005', 'U1005', '9793333333333', DATE '2025-03-25', 3, 1, 0, 'Returned');

INSERT INTO BorrowedBook (BorrowedID, UserID, BookID, IssueDate, TotalRenewals, RenewalsLeft, Fine, Status)
VALUES ('1000000006', 'U1001', '9791111111111', DATE '2025-04-21', 3, 3, 0, 'Borrowed');

INSERT INTO Reservation VALUES ('R001', 'U1001', '9781234567890', DATE '2025-04-01', DATE '2025-04-10', 'Reserved');
INSERT INTO Reservation VALUES ('R002', 'U1002', '9780987654321', DATE '2025-04-02', DATE '2025-04-11', 'Cancelled');
INSERT INTO Reservation VALUES ('R003', 'U1003', '9791111111111', DATE '2025-04-03', DATE '2025-04-12', 'Fulfilled');
INSERT INTO Reservation VALUES ('R004', 'U1004', '9782222222222', DATE '2025-04-04', DATE '2025-04-13', 'Reserved');
INSERT INTO Reservation VALUES ('R005', 'U1005', '9793333333333', DATE '2025-04-05', DATE '2025-04-14', 'Reserved');
INSERT INTO Reservation VALUES ('R006', 'U1001', '9782222222222', DATE '2025-04-21', DATE '2025-04-28', 'Reserved');

INSERT INTO CartCollection VALUES ('U1001', '9781234567890', DATE '2025-04-01', DATE '2025-04-05', 'not finished');
INSERT INTO CartCollection VALUES ('U1002', '9780987654321', DATE '2025-04-02', DATE '2025-04-06', 'finished');
INSERT INTO CartCollection VALUES ('U1003', '9791111111111', DATE '2025-04-03', DATE '2025-04-07', 'not finished');
INSERT INTO CartCollection VALUES ('U1004', '9782222222222', DATE '2025-04-04', DATE '2025-04-08', 'finished');
INSERT INTO CartCollection VALUES ('U1005', '9793333333333', DATE '2025-04-05', DATE '2025-04-09', 'not finished');
INSERT INTO CartCollection VALUES ('U1001', '9780987654321', DATE '2025-04-21', DATE '2025-04-24', 'not finished');


INSERT INTO RequestBook VALUES ('9781234567890', DATE '2025-04-01', 'New Edition', DATE '2025-04-03', 'Pending');
INSERT INTO RequestBook VALUES ('9780987654321', DATE '2025-04-02', 'Additional Copies', DATE '2025-04-05', 'Approved');
INSERT INTO RequestBook VALUES ('9791111111111', DATE '2025-04-03', 'Reprint', NULL, 'Pending');
INSERT INTO RequestBook VALUES ('9782222222222', DATE '2025-04-04', 'Add to Collection', DATE '2025-04-06', 'Approved');
INSERT INTO RequestBook VALUES ('9793333333333', DATE '2025-04-05', 'Replacement', NULL, 'Pending');
INSERT INTO RequestBook VALUES ('9780987654321', DATE '2025-04-21', 'New Edition', DATE '2025-06-12', 'Pending');

INSERT INTO RequestBookWithDetails VALUES ('U1001', DATE '2025-04-01', '9781234567890', 'Introduction to Algorithms', 'Thomas H. Cormen');
INSERT INTO RequestBookWithDetails VALUES ('U1002', DATE '2025-04-02', '9780987654321', 'Database System Concepts', 'Abraham Silberschatz');
INSERT INTO RequestBookWithDetails VALUES ('U1003', DATE '2025-04-03', '9791111111111', 'Artificial Intelligence: A Modern Approach', 'Stuart Russell');
INSERT INTO RequestBookWithDetails VALUES ('U1004', DATE '2025-04-04', '9782222222222', 'Operating Systems', 'Andrew S. Tanenbaum');
INSERT INTO RequestBookWithDetails VALUES ('U1005', DATE '2025-04-05', '9793333333333', 'Computer Networks', 'James F. Kurose');
INSERT INTO RequestBookWithDetails VALUES ('U1001', DATE '2025-04-21', '9780987654321', 'Computer Networks', 'Elaine Marieb');

INSERT INTO Rating VALUES ('U1001', '9781234567890', 5, DATE '2025-04-01');
INSERT INTO Rating VALUES ('U1002', '9780987654321', 4, DATE '2025-04-02');
INSERT INTO Rating VALUES ('U1003', '9791111111111', 3, DATE '2025-04-03');
INSERT INTO Rating VALUES ('U1004', '9782222222222', 4, DATE '2025-04-04');
INSERT INTO Rating VALUES ('U1005', '9793333333333', 5, DATE '2025-04-05');

INSERT INTO Feedback VALUES ('U1001', DATE '2025-04-01', 'Great selection of books and quick service!', 5, DATE '2025-04-02');
INSERT INTO Feedback VALUES ('U1002', DATE '2025-04-03', 'Could improve the online search feature.', 3, NULL);
INSERT INTO Feedback VALUES ('U1003', DATE '2025-04-05', 'Helpful staff and clean environment.', 4, DATE '2025-04-06');
INSERT INTO Feedback VALUES ('U1004', DATE '2025-04-07', 'More study rooms needed during exams.', 4, DATE '2025-06-01');
INSERT INTO Feedback VALUES ('U1005', DATE '2025-04-09', 'I love the new digital borrowing system.', 5, DATE '2025-06-01');
INSERT INTO Feedback VALUES ('U1001', DATE '2025-04-21', 'The overall library services are Good!!!', 5, DATE '2025-06-12');


-- Sample data
INSERT INTO HelpdeskRequests (UserID, RequestDate, IssueType, Description, Status, Response, ResponseDate)
VALUES ('U1001', DATE '2025-06-01', 'Book Availability', 'Cannot find book "Database Systems" in catalog', 'Resolved', 'Book is available in Section A-12', DATE '2025-06-02');

INSERT INTO HelpdeskRequests (UserID, RequestDate, IssueType, Description, Status)
VALUES ('U1002', DATE '2025-06-03', 'Technical Problems', 'Website shows error when trying to renew books', 'Pending');

INSERT INTO HelpdeskRequests (UserID, RequestDate, IssueType, Description, Status, Response, ResponseDate)
VALUES ('U1003', DATE '2025-06-05', 'Membership Issues', 'Membership card not working', 'Resolved', 'Card reactivated, please try now', DATE '2025-06-05');

INSERT INTO HelpdeskRequests (UserID, RequestDate, IssueType, Description, Status)
VALUES ('U1004', DATE '2025-06-07', 'Billing Issues', 'Charged incorrect late fee', 'In Progress');

INSERT INTO HelpdeskRequests (UserID, RequestDate, IssueType, Description, Status, Response, ResponseDate)
VALUES ('U1005', DATE '2025-06-10', 'Other', 'Need research assistance', 'Resolved', 'Librarian will contact you', DATE '2025-06-11');