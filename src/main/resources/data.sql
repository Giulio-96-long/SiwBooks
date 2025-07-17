-- ==============================
-- UTENTI 
-- ==============================
INSERT INTO `users` ( first_name, last_name) VALUES
  ('Admin',   'User'),
  ('Regular', 'User'),
  ('Alice',   'Bianchi'),
  ('Bob',     'Verdi'),
  ('Carla',   'Neri'),
  ('Dario',   'Blu');

-- ==============================
-- CREDENTIALS
-- ==============================
INSERT INTO credentials (email, password, role, user_id) VALUES
  ('admin@email.it',     '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'ADMIN', 1),
  ('user@email.it',      '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'USER',  2),
  ('alice@example.com',  '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'USER',  3),
  ('bob@example.com',    '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'USER',  4),
  ('carla@example.com',  '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'USER',  5),
  ('dario@example.com',  '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'USER',  6);

-- ==============================
-- AUTORI REALI
-- ==============================
INSERT INTO author (first_name, last_name, birth_date, death_date, nationality, photo, photo_content_type) VALUES
  ('J.K.',           'Rowling',           '1965-07-31', NULL,           'Britannica', NULL, NULL),
  ('George',         'Orwell',            '1903-06-25', '1950-01-21',  'Britannica', NULL, NULL),
  ('Jane',           'Austen',            '1775-12-16', '1817-07-18',  'Britannica', NULL, NULL),
  ('Italo',          'Calvino',           '1923-10-15', '1985-09-19',  'Italiana',   NULL, NULL),
  ('Umberto',        'Eco',               '1932-01-05', '2016-02-19',  'Italiana',   NULL, NULL),
  ('Gabriel García', 'Márquez',           '1927-03-06', '2014-04-17',  'Colombiana', NULL, NULL);

-- ==============================
-- LIBRI REALI
-- ==============================
INSERT INTO book (title, publication_year) VALUES
  ('Harry Potter and the Philosopher''s Stone', 1997),
  ('1984',                                       1949),
  ('Pride and Prejudice',                        1813),
  ('Invisible Cities',                           1972),
  ('The Name of the Rose',                       1980),
  ('One Hundred Years of Solitude',              1967);

-- ==============================
-- ASSOCIAZIONI LIBRO–AUTORE
-- ==============================
INSERT INTO book_author (book_id, author_id) VALUES
  (1, 1),  -- Rowling → Harry Potter
  (2, 2),  -- Orwell  → 1984
  (3, 3),  -- Austen  → Pride & Prejudice
  (4, 4),  -- Calvino → Invisible Cities
  (5, 5),  -- Eco     → The Name of the Rose
  (6, 6);  -- Márquez → One Hundred Years ...

-- ==============================
-- RECENSIONI DI ESEMPIO
-- ==============================
INSERT INTO book_review (title, rating, text, created_at, user_id, book_id) VALUES
  -- Harry Potter
  ('Magico e coinvolgente',   5, 'Un inizio meraviglioso per la saga.', NOW(), 3, 1),
  ('Bella scoperta',          4, 'Ottimo worldbuilding, ma qualche lentezza.', NOW(), 4, 1),
  ('Adatto ai giovani',       3, 'Carino ma semplice.', NOW(), 5, 1),
  ('Poco impegnativo',        2, 'Mi aspettavo di più dal finale.', NOW(), 6, 1),

  -- 1984
  ('Distopico e attuale',     5, 'Un classico imprescindibile.', NOW(), 3, 2),
  ('Profondo ma duro',        4, 'Bella scrittura, tema pesante.', NOW(), 4, 2),
  ('Difficile da digerire',   3, 'Interessante, ma deprimente.', NOW(), 5, 2),
  ('Fenomenale',              5, 'Ancora oggi straordinario.', NOW(), 6, 2),

  -- Pride & Prejudice
  ('Romantico e ironico',     5, 'Dialoghi brillanti e personaggi vividi.', NOW(), 3, 3),
  ('Intramontabile',          5, 'Una lettura che non stanca mai.', NOW(), 4, 3),
  ('Elegante ma lento',       3, 'Talvolta prolisso.', NOW(), 5, 3),
  ('Un capolavoro',           5, 'Jane Austen al massimo.', NOW(), 6, 3),

  -- Invisible Cities
  ('Visionario',              5, 'Ogni città un piccolo universo.', NOW(), 3, 4),
  ('Poetico ma astratto',     4, 'Bellissime descrizioni, trama leggera.', NOW(), 4, 4),
  ('Richiede riflessione',    4, 'Non è un romanzo tradizionale.', NOW(), 5, 4),
  ('Capolavoro breve',        5, 'Intenso e memorabile.', NOW(), 6, 4),

  -- The Name of the Rose
  ('Avvincente e colto',      5, 'Una detective medievale da antologia.', NOW(), 3, 5),
  ('Storico e misterioso',    4, 'Ottima ricostruzione storica.', NOW(), 4, 5),
  ('Un po’ lento',            3, 'Parte centrale un po’ pesante.', NOW(), 5, 5),
  ('Emozionante',             5, 'Suspense fino all’ultima pagina.', NOW(), 6, 5),

  -- One Hundred Years of Solitude
  ('Magia pura',              5, 'Realismo magico al suo meglio.', NOW(), 3, 6),
  ('Fluido e coinvolgente',    4, 'Generazioni indimenticabili.', NOW(), 4, 6),
  ('Complesso ma bello',      4, 'A volte difficile da seguire.', NOW(), 5, 6),
  ('Straordinario',           5, 'Un caposaldo della letteratura mondiale.', NOW(), 6, 6);
