CREATE TABLE client (
    id_client BIGSERIAL PRIMARY KEY,
    company BIGINT NOT NULL,
    name_client VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(100) NOT NULL,
    alias VARCHAR(100) NOT NULL,
    description VARCHAR(500),

    CONSTRAINT uq_client_company_phone UNIQUE (company, phone)
);

CREATE INDEX idx_client_company ON public.client(company);
CREATE INDEX idx_client_phone ON public.client(phone);

CREATE TABLE product (
    id_product BIGSERIAL NOT NULL,
    company BIGINT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(500),
    color VARCHAR(5000),
    price NUMERIC(12,2) NOT NULL,
    status BOOLEAN DEFAULT TRUE,

    CONSTRAINT pk_product
        PRIMARY KEY (id_product, company)
);

CREATE INDEX idx_product_company ON product(company);

CREATE TABLE orden (
    id_orden BIGSERIAL NOT NULL,
    company BIGINT NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    orden_date NUMERIC(8,0) NOT NULL, -- YYYYMMDD
    address TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    description VARCHAR(500),

    CONSTRAINT pk_orden
        PRIMARY KEY (id_orden, company, orden_date)
);

CREATE INDEX idx_orden_company ON orden(company);
CREATE INDEX idx_orden_date ON orden(orden_date);

CREATE TABLE stock (
    id_stock BIGSERIAL NOT NULL,
    company BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name TEXT NOT NULL,
    quantity INTEGER NOT NULL,
    date_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(500),

    CONSTRAINT pk_stock
        PRIMARY KEY (id_stock, company, product_id),

    CONSTRAINT fk_stock_product
        FOREIGN KEY (product_id, company)
        REFERENCES product (id_product, company)
);

CREATE INDEX idx_stock_company ON stock(company);
CREATE INDEX idx_stock_product ON stock(product_id);

ALTER TABLE orden
ADD CONSTRAINT uq_orden_id_company
UNIQUE (id_orden, company);

CREATE TABLE detail_orden (
    id_detail BIGSERIAL NOT NULL,
    company BIGINT NOT NULL,
    orden_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    amount NUMERIC(12,2) NOT NULL,

    CONSTRAINT pk_detail_orden
        PRIMARY KEY (id_detail, company, orden_id),

    CONSTRAINT fk_detail_orden
        FOREIGN KEY (orden_id, company)
        REFERENCES orden (id_orden, company),

    CONSTRAINT fk_detail_product
        FOREIGN KEY (product_id, company)
        REFERENCES product (id_product, company)

    CONSTRAINT fk_detail_client
        FOREIGN KEY (client_id)
        REFERENCES client (id_client)
);

CREATE INDEX idx_detail_orden_company ON detail_orden(company);
CREATE INDEX idx_detail_orden_orden ON detail_orden(orden_id);
CREATE INDEX idx_detail_orden_product ON detail_orden(product_id);
CREATE INDEX idx_detail_orden_client ON detail_orden(client_id);

ALTER TABLE stock
ADD CONSTRAINT chk_stock_quantity
CHECK (quantity >= 0);

ALTER TABLE detail_orden
ADD CONSTRAINT chk_detail_quantity
CHECK (quantity > 0);

ALTER TABLE product
ADD COLUMN image_base64 TEXT;

ALTER TABLE product RENAME COLUMN descripcion TO description;
ALTER TABLE orden ADD phone varchar(20) NOT NULL;
ALTER TABLE detail_orden ADD priceunit numeric(12, 2) NOT NULL;
ALTER TABLE public.client ADD address varchar(100) NOT NULL;
ALTER TABLE public.orden DROP COLUMN address;
ALTER TABLE public.orden DROP COLUMN phone;

ALTER TABLE orden ADD subtotal  NUMERIC(12,2) NOT NULL;
ALTER TABLE orden ADD total  NUMERIC(12,2) NOT NULL;


ALTER TABLE orden
    ADD COLUMN client_id BIGINT NOT NULL;

ALTER TABLE orden
    ADD CONSTRAINT fk_orden_client
        FOREIGN KEY (client_id)
            REFERENCES public.client (id_client);

