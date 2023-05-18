CREATE TABLE IF NOT EXISTS pay_box_entry (
                         id SERIAL PRIMARY KEY,
                         entry_type INT NOT NULL,
                         entry_date timestamp NOT NULL,
                         entry_value numeric not null,
                         pay_box_total numeric NOT NULL
);


