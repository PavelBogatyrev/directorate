alter table kpi add	index kpi_category_idx (category);
alter table kpi add	index kpi_name_idx (name);
alter table kpi add	index kpi_quarter_idx (period);
alter table kpi add	index kpi_year_idx (year);
alter table kpi add	index kpi_location_idx (location);

alter table client_quarter_revenue add 	index client_quarter_revenue_quarter_idx (period);

alter table currency_expenses add index currency_expenses_quarter_idx (period);
alter table currency_expenses add index currency_expenses_hidden_idx (hidden);

alter table currency_revenue  add index currency_revenue_quarter_idx (period);
alter table currency_revenue  add index currency_revenue_hidden_idx (hidden);
