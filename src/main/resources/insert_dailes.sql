delete from quote_attempts where quote_id IN (select id from quotes where daily='t');
delete from quotes where daily='t';
Insert into quotes(text,language,origin,accepted,daily,created_at,updated_at) Values('Einhver svona nokkur orð' ,0 ,'Krissi',true,true,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
Insert into quotes(text,language,origin,accepted,daily,created_at,updated_at) Values('Já ég er nú bara að fetcha data hérna' ,0 ,'Ívan',true,true,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
Insert into quotes(text,language,origin,accepted,daily,created_at,updated_at) Values('Þetta er nú bara eitthvað bullshit' ,0 ,'Frikki',true,true,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
Insert into quotes(text,language,origin,accepted,daily,created_at,updated_at) Values('Vel góð áskorun' ,0 ,'Frikki',true,true,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
Insert into quotes(text,language,origin,accepted,daily,created_at,updated_at) Values('Það er Consultation í dag' ,0 ,'Frikki',true,true,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
Insert into quotes(text,language,origin,accepted,daily,created_at,updated_at) Values('Helvítis djöfulsins andskotans bölvans fokking dót' ,0 ,'Valdi Pottþétt',true,true,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
