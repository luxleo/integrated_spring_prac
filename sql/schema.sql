drop table tempMember if exists cascade;
create table tempMember (
                        member_id varchar(10),
                        money integer not null default 0,
                        primary key (member_id)
);

insert into tempMember(member_id, money) values ('hi1',10000);
insert into tempMember(member_id, money) values ('hi2',20000);