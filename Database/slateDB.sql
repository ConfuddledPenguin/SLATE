CREATE TABLE Assignment (
  Id             bigint(20) NOT NULL AUTO_INCREMENT, 
  assignmentNo   int(11) NOT NULL, 
  name           varchar(255), 
  module         bigint(20), 
  dueDate        datetime NULL, 
  markMeanMax    double, 
  markMeanMean   double, 
  markMeanMin    double, 
  markMeanStdDev double, 
  markMeanTotal  int(11) DEFAULT 0, 
  PRIMARY KEY (Id)) ENGINE=InnoDB;
CREATE TABLE AssignmentMark (
  id         bigint(20) NOT NULL AUTO_INCREMENT, 
  percentage int(11) NOT NULL, 
  assignment bigint(20) NOT NULL, 
  username   varchar(255) NOT NULL, 
  PRIMARY KEY (id)) ENGINE=InnoDB;
CREATE TABLE Attendance (
  id         bigint(20) NOT NULL AUTO_INCREMENT, 
  `session`  bigint(20), 
  `user`     varchar(255), 
  attendance varchar(255), 
  PRIMARY KEY (id)) ENGINE=InnoDB;
CREATE TABLE AttendanceGrouping (
  Id                      bigint(20) NOT NULL AUTO_INCREMENT, 
  sessionType             varchar(255), 
  attendanceAverageMax    double, 
  attendanceAverageMean   double, 
  attendanceAverageMin    double, 
  attendanceAverageStdDev double, 
  attendanceAverageTotal  double, 
  PRIMARY KEY (Id)) ENGINE=InnoDB;
CREATE TABLE AttendanceGrouping_weeklyMeans (
  attendanceGrouping_Id bigint(20) NOT NULL, 
  max                   double NOT NULL, 
  mean                  double NOT NULL, 
  min                   double NOT NULL, 
  stdDev                double NOT NULL, 
  orderIndex            int(11) NOT NULL, 
  total                 double NOT NULL) ENGINE=InnoDB;
CREATE TABLE Correlation (
  Id              bigint(20) NOT NULL AUTO_INCREMENT, 
  linearIntercept double NOT NULL, 
  linearSlope     double NOT NULL, 
  pearson         double NOT NULL, 
  sessionType     varchar(255), 
  PRIMARY KEY (Id)) ENGINE=InnoDB;
CREATE TABLE Enrollment (
  Id                        int(11) NOT NULL AUTO_INCREMENT, 
  finalMark                 double NOT NULL, 
  result                    varchar(255), 
  module                    bigint(20), 
  `user`                    varchar(255), 
  attainmentGoal            int(11) NOT NULL, 
  attendanceGoal            int(11) NOT NULL, 
  assignmentMax             double, 
  assignmentMean            double, 
  assignmentMin             double, 
  assignmentStdDev          double, 
  assignmentTotal           int(11) DEFAULT 0, 
  predictedGrade_attendance double NOT NULL, 
  PRIMARY KEY (Id)) ENGINE=InnoDB;
CREATE TABLE EnrollmentAttendanceGroupings (
  attendanceGroupingId int(11) NOT NULL, 
  enrollmentId         bigint(20) NOT NULL, 
  PRIMARY KEY (attendanceGroupingId, 
  enrollmentId)) ENGINE=InnoDB;
CREATE TABLE EnrollmentPredictedGradeAttendance (
  enrollmentId int(11) NOT NULL, 
  predictionId bigint(20) NOT NULL, 
  PRIMARY KEY (enrollmentId, 
  predictionId)) ENGINE=InnoDB;
CREATE TABLE Messages (
  Id   bigint(20) NOT NULL, 
  name varchar(255), 
  text text, 
  type varchar(255)) ENGINE=InnoDB;
CREATE TABLE Module (
  id                      bigint(20) NOT NULL AUTO_INCREMENT, 
  classCode               varchar(255), 
  description             text, 
  name                    varchar(255), 
  passRate                double NOT NULL, 
  noStudents              int(11) NOT NULL, 
  attendanceAverageMax    double, 
  attendanceAverageMean   double, 
  attendanceAverageMin    double, 
  attendanceAverageStdDev double, 
  classAverageMax         double, 
  classAverageMean        double, 
  classAverageMin         double, 
  classAverageStdDev      double, 
  analysed                bit(1) NOT NULL, 
  attendanceAverageTotal  double, 
  classAverageTotal       double, 
  moduleLevel             int(11) NOT NULL, 
  attainmentGoal          int(11) NOT NULL, 
  attendanceGoal          int(11) NOT NULL, 
  PRIMARY KEY (id)) ENGINE=InnoDB;
CREATE TABLE ModuleAttendanceAttainmentCorrelations (
  moduleId      bigint(20) NOT NULL, 
  correlationId bigint(20) NOT NULL, 
  PRIMARY KEY (moduleId, 
  correlationId)) ENGINE=InnoDB;
CREATE TABLE ModuleAttendanceGroupings (
  AttendanceGroupingId bigint(20) NOT NULL, 
  ModuleId             bigint(20) NOT NULL, 
  PRIMARY KEY (AttendanceGroupingId, 
  ModuleId)) ENGINE=InnoDB;
CREATE TABLE ModuleYear (
  id                    bigint(20) NOT NULL AUTO_INCREMENT, 
  classCode             varchar(255), 
  year                  varchar(255), 
  module                bigint(20), 
  passRate              double NOT NULL, 
  finalMarkMax          double, 
  finalMarkMean         double, 
  finalMarkMin          double, 
  finalMarkStdDev       double, 
  finalMarkAverageTotal double, 
  analysed              bit(1) NOT NULL, 
  noStudents            int(11) NOT NULL, 
  warningsGenerated     bit(1) NOT NULL, 
  PRIMARY KEY (id)) ENGINE=InnoDB;
CREATE TABLE ModuleYearAttendanceAttainmentCorrelations (
  moduleId      bigint(20) NOT NULL, 
  correlationId bigint(20) NOT NULL, 
  PRIMARY KEY (moduleId, 
  correlationId)) ENGINE=InnoDB;
CREATE TABLE ModuleYearAttendanceGroupings (
  attendanceGroupingId bigint(20) NOT NULL, 
  moduleId             bigint(20) NOT NULL, 
  PRIMARY KEY (attendanceGroupingId, 
  moduleId)) ENGINE=InnoDB;
CREATE TABLE Prediction (
  id                     bigint(20) NOT NULL AUTO_INCREMENT, 
  correlationCoefficient double NOT NULL, 
  meanAbsError           double NOT NULL, 
  predicted_value        double NOT NULL, 
  relativeAbsError       double NOT NULL, 
  rootMeanAbsError       double NOT NULL, 
  rootRelativeAbsError   double NOT NULL, 
  total                  int(11) NOT NULL, 
  predictionType         varchar(255), 
  PRIMARY KEY (id)) ENGINE=InnoDB;
CREATE TABLE SLATE_State (
  state_name  varchar(255) NOT NULL, 
  state_value varchar(255)) ENGINE=InnoDB;
CREATE TABLE SLATE_Statistics (
  Id    varchar(255) NOT NULL, 
  name  varchar(255), 
  value double NOT NULL) ENGINE=InnoDB;
CREATE TABLE `Session` (
  id                   bigint(20) NOT NULL AUTO_INCREMENT, 
  `date`               datetime NULL, 
  sessionType          varchar(255), 
  module               bigint(20), 
  analysed             bit(1) NOT NULL, 
  weekDate             datetime NULL, 
  attendanceMeanMax    double, 
  attendanceMeanMean   double, 
  attendanceMeanMin    double, 
  attendanceMeanStdDev double, 
  enrolled             int(11) NOT NULL, 
  present              int(11) NOT NULL, 
  weekNo               int(11) NOT NULL, 
  total                double NOT NULL, 
  PRIMARY KEY (id)) ENGINE=InnoDB;
CREATE TABLE `User` (
  username       varchar(255) NOT NULL, 
  course         varchar(255), 
  email          varchar(255), 
  password       varchar(255), 
  role           varchar(255), 
  year           int(11) NOT NULL, 
  name           varchar(255), 
  attainmentGoal int(11) NOT NULL, 
  attendanceGoal int(11) NOT NULL, 
  PRIMARY KEY (username)) ENGINE=InnoDB;
CREATE TABLE hibernate_sequence (
  next_val bigint(20)) ENGINE=InnoDB;
CREATE TABLE moduleTeaching (
  username varchar(255) NOT NULL, 
  id       bigint(20) NOT NULL, 
  PRIMARY KEY (username, 
  id)) ENGINE=InnoDB;
CREATE INDEX FKuvmvpuwvikjolpv8mgo5ob3l 
  ON Assignment (module);
CREATE UNIQUE INDEX UK_3nhwufwvtsmgsbjk8rm2yn8mq 
  ON EnrollmentAttendanceGroupings (enrollmentId);
CREATE UNIQUE INDEX UK_81h8ve3lh3rub59t8keai6nei 
  ON EnrollmentPredictedGradeAttendance (predictionId);
CREATE INDEX id 
  ON Module (id);
CREATE INDEX classCode 
  ON Module (classCode);
CREATE UNIQUE INDEX UK_esjfx9uhu8eq0gwy4mwto6ahs 
  ON ModuleAttendanceAttainmentCorrelations (correlationId);
CREATE UNIQUE INDEX UK_ctxyp7etidd4lwj5m378motxr 
  ON ModuleAttendanceGroupings (ModuleId);
CREATE UNIQUE INDEX UK_52121euk2uamhyh5ktk46xxhn 
  ON ModuleAttendanceGroupings (ModuleId);
CREATE INDEX classCode 
  ON ModuleYear (classCode);
CREATE INDEX year 
  ON ModuleYear (year);
CREATE UNIQUE INDEX UK_oheeh3g93oatsm32g6x0t1ys8 
  ON ModuleYearAttendanceAttainmentCorrelations (correlationId);
CREATE UNIQUE INDEX UK_83bpq5hufw6exoudoedfs74yn 
  ON ModuleYearAttendanceGroupings (moduleId);
ALTER TABLE Assignment ADD INDEX FK720r97radt99yfd9y328rrsbs (module), ADD CONSTRAINT FK720r97radt99yfd9y328rrsbs FOREIGN KEY (module) REFERENCES ModuleYear (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE AssignmentMark ADD INDEX FKif9jc8n0wadh19g7u4o332que (assignment), ADD CONSTRAINT FKif9jc8n0wadh19g7u4o332que FOREIGN KEY (assignment) REFERENCES Assignment (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE AssignmentMark ADD INDEX FKl7jegbsvt23nyv2k1g3ohf4ab (username), ADD CONSTRAINT FKl7jegbsvt23nyv2k1g3ohf4ab FOREIGN KEY (username) REFERENCES `User` (username) ON UPDATE No action ON DELETE No action;
ALTER TABLE Attendance ADD INDEX FKof5amarqjtm8smf8l3au8ydrf (`session`), ADD CONSTRAINT FKof5amarqjtm8smf8l3au8ydrf FOREIGN KEY (`session`) REFERENCES `Session` (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE Attendance ADD INDEX FK308dph3crjweweylfsvpeshy6 (`user`), ADD CONSTRAINT FK308dph3crjweweylfsvpeshy6 FOREIGN KEY (`user`) REFERENCES `User` (username) ON UPDATE No action ON DELETE No action;
ALTER TABLE AttendanceGrouping_weeklyMeans ADD INDEX FK2keagnewsge2s3ia44js7hjh1 (attendanceGrouping_Id), ADD CONSTRAINT FK2keagnewsge2s3ia44js7hjh1 FOREIGN KEY (attendanceGrouping_Id) REFERENCES AttendanceGrouping (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE AttendanceGrouping_weeklyMeans ADD INDEX FKkk4rqt1kdjg7jh8d1ryrvsxhx (attendanceGrouping_Id), ADD CONSTRAINT FKkk4rqt1kdjg7jh8d1ryrvsxhx FOREIGN KEY (attendanceGrouping_Id) REFERENCES AttendanceGrouping (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE Enrollment ADD INDEX FKie7wdu0blw554ljli8g4pw3rp (module), ADD CONSTRAINT FKie7wdu0blw554ljli8g4pw3rp FOREIGN KEY (module) REFERENCES ModuleYear (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE Enrollment ADD INDEX FKpv8y819r8wtjx80ns18rv2s9l (module), ADD CONSTRAINT FKpv8y819r8wtjx80ns18rv2s9l FOREIGN KEY (module) REFERENCES Module (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE Enrollment ADD INDEX FKl378vt2fcibul1fho6m0u3nq0 (`user`), ADD CONSTRAINT FKl378vt2fcibul1fho6m0u3nq0 FOREIGN KEY (`user`) REFERENCES `User` (username) ON UPDATE No action ON DELETE No action;
ALTER TABLE EnrollmentAttendanceGroupings ADD INDEX FKc2isxxewx2lchp6xdc6y4kow8 (attendanceGroupingId), ADD CONSTRAINT FKc2isxxewx2lchp6xdc6y4kow8 FOREIGN KEY (attendanceGroupingId) REFERENCES Enrollment (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE EnrollmentAttendanceGroupings ADD INDEX FKk3rqjeq3qag9at6k8m60i9vxm (enrollmentId), ADD CONSTRAINT FKk3rqjeq3qag9at6k8m60i9vxm FOREIGN KEY (enrollmentId) REFERENCES AttendanceGrouping (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE EnrollmentPredictedGradeAttendance ADD INDEX FKq3djaks9o2y02v1p6o6dvbbek (enrollmentId), ADD CONSTRAINT FKq3djaks9o2y02v1p6o6dvbbek FOREIGN KEY (enrollmentId) REFERENCES Enrollment (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE EnrollmentPredictedGradeAttendance ADD INDEX FK6cvo7jl79rhi64ks1uxbjw6f9 (predictionId), ADD CONSTRAINT FK6cvo7jl79rhi64ks1uxbjw6f9 FOREIGN KEY (predictionId) REFERENCES Prediction (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleAttendanceAttainmentCorrelations ADD INDEX FKtf400c8xihyqnyhrmiu81h4ok (moduleId), ADD CONSTRAINT FKtf400c8xihyqnyhrmiu81h4ok FOREIGN KEY (moduleId) REFERENCES Module (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleAttendanceAttainmentCorrelations ADD INDEX FKgeygv28uw7p35da72323n41el (correlationId), ADD CONSTRAINT FKgeygv28uw7p35da72323n41el FOREIGN KEY (correlationId) REFERENCES Correlation (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleAttendanceGroupings ADD INDEX FKk4oe004p24yxx5fv09upxn31e (AttendanceGroupingId), ADD CONSTRAINT FKk4oe004p24yxx5fv09upxn31e FOREIGN KEY (AttendanceGroupingId) REFERENCES Module (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleAttendanceGroupings ADD INDEX FKkqjycvkkt92mn97j2g7ppalyb (AttendanceGroupingId), ADD CONSTRAINT FKkqjycvkkt92mn97j2g7ppalyb FOREIGN KEY (AttendanceGroupingId) REFERENCES Module (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleAttendanceGroupings ADD INDEX FK2u4mkpfv3i9rnj0embvg0ovqi (ModuleId), ADD CONSTRAINT FK2u4mkpfv3i9rnj0embvg0ovqi FOREIGN KEY (ModuleId) REFERENCES AttendanceGrouping (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleAttendanceGroupings ADD INDEX FKlsd35h9nk9ltk219pm23v8ueo (ModuleId), ADD CONSTRAINT FKlsd35h9nk9ltk219pm23v8ueo FOREIGN KEY (ModuleId) REFERENCES AttendanceGrouping (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleAttendanceGroupings ADD INDEX FKotta43mup5pq3g00d62e2gxhr (ModuleId), ADD CONSTRAINT FKotta43mup5pq3g00d62e2gxhr FOREIGN KEY (ModuleId) REFERENCES AttendanceGrouping (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleYear ADD INDEX FK4l3dxtdek21tx7n7he3yyvyp2 (module), ADD CONSTRAINT FK4l3dxtdek21tx7n7he3yyvyp2 FOREIGN KEY (module) REFERENCES Module (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleYearAttendanceAttainmentCorrelations ADD INDEX FK9nsbtkaus8k7bc7mahv6neejp (moduleId), ADD CONSTRAINT FK9nsbtkaus8k7bc7mahv6neejp FOREIGN KEY (moduleId) REFERENCES ModuleYear (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleYearAttendanceAttainmentCorrelations ADD INDEX FK6953vwwbqym5mwbxdnfe4a567 (correlationId), ADD CONSTRAINT FK6953vwwbqym5mwbxdnfe4a567 FOREIGN KEY (correlationId) REFERENCES Correlation (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleYearAttendanceGroupings ADD INDEX FKctk6vr65dtnc1s6n1sa7mbekh (attendanceGroupingId), ADD CONSTRAINT FKctk6vr65dtnc1s6n1sa7mbekh FOREIGN KEY (attendanceGroupingId) REFERENCES ModuleYear (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE ModuleYearAttendanceGroupings ADD INDEX FKqwfhx9j27vwhxnyt686mq9dox (moduleId), ADD CONSTRAINT FKqwfhx9j27vwhxnyt686mq9dox FOREIGN KEY (moduleId) REFERENCES AttendanceGrouping (Id) ON UPDATE No action ON DELETE No action;
ALTER TABLE `Session` ADD INDEX FKjkxkksf2vihh5mqlog4acrhcc (module), ADD CONSTRAINT FKjkxkksf2vihh5mqlog4acrhcc FOREIGN KEY (module) REFERENCES Module (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE `Session` ADD INDEX FKnjckad44o6i3dcbvyi5bsxfpe (module), ADD CONSTRAINT FKnjckad44o6i3dcbvyi5bsxfpe FOREIGN KEY (module) REFERENCES ModuleYear (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE moduleTeaching ADD INDEX FKciq0uuen99ulm9aus0ycegiva (username), ADD CONSTRAINT FKciq0uuen99ulm9aus0ycegiva FOREIGN KEY (username) REFERENCES `User` (username) ON UPDATE No action ON DELETE No action;
ALTER TABLE moduleTeaching ADD INDEX FK1hx6uoo1loubqjqpfm429llve (id), ADD CONSTRAINT FK1hx6uoo1loubqjqpfm429llve FOREIGN KEY (id) REFERENCES Module (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE moduleTeaching ADD INDEX FK9irx7qou9cb137avrmki476do (id), ADD CONSTRAINT FK9irx7qou9cb137avrmki476do FOREIGN KEY (id) REFERENCES ModuleYear (id) ON UPDATE No action ON DELETE No action;
