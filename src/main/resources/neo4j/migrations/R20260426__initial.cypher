// (:Company)
CREATE CONSTRAINT company_id_unique
IF NOT EXISTS FOR (c:Company) REQUIRE c.id IS UNIQUE;

CREATE CONSTRAINT company_tin_unique
IF NOT EXISTS FOR (c:Company) REQUIRE c.tin IS UNIQUE;

CREATE INDEX company_domain
IF NOT EXISTS FOR (n:Company) ON (n.domain);

CREATE INDEX company_registration_date
IF NOT EXISTS FOR (n:Company) ON (n.registrationDate);

CREATE FULLTEXT INDEX company_search
IF NOT EXISTS FOR (n:Company) ON EACH [n.name, n.description];

// (:Comapany)-[:HAS_EMPLOYEE]->(:Person)
CREATE INDEX employee_position
IF NOT EXISTS FOR () - [r:HAS_EMPLOYEE] - () ON (r.position);

CREATE INDEX employee_since
IF NOT EXISTS FOR ()- [r:HAS_EMPLOYEE] -() ON (r.since);

CREATE INDEX employee_until
IF NOT EXISTS FOR ()- [r:HAS_EMPLOYEE] -() ON (r.until);

// (:Person)
CREATE CONSTRAINT person_id_unique
IF NOT EXISTS FOR (p:Person) REQUIRE p.id IS UNIQUE;

CREATE CONSTRAINT person_email_unique
IF NOT EXISTS FOR (p:Person) REQUIRE p.email IS UNIQUE;

// (:Person)-[:HAS_TASK]->(:Task)
CREATE INDEX task_assignment_role
IF NOT EXISTS FOR () - [r:HAS_TASK] - () ON (r.role);

CREATE INDEX task_assignment_since
IF NOT EXISTS FOR () - [r:HAS_TASK] - () ON (r.since);

CREATE INDEX task_assignment_until
IF NOT EXISTS FOR () - [r:HAS_TASK] - () ON (r.until);

// (:Task)
CREATE CONSTRAINT task_id_unique
IF NOT EXISTS FOR (t:Task) REQUIRE t.id IS UNIQUE;

CREATE INDEX task_status
IF NOT EXISTS FOR (t:Task) ON (t.status);

CREATE INDEX task_priority
IF NOT EXISTS FOR (t:Task) ON (t.priority);

CREATE FULLTEXT INDEX task_search
IF NOT EXISTS FOR (n:Task) ON EACH [n.title, n.description];