select s.id, s.page, s.period, s.status from screenshot s where s.period  = '2016Q2' order by page

select s.id, s.page, s.period, s.status from screenshot s order by status desc

-- Cleint
select * from client_quarter_revenue where period = '2016Q2' order by revenue desc

select * from client

update screenshot set status = 2

select * from kpi where period = '2016Q4' and name = 'Delivery HC'

delete from screenshot

