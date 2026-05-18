USE vietlocal;

UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1569154941061-e231b4725ef1?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'da-nang';
UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1605538032432-a9f0c8d9baac?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'ha-noi';
UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'sa-pa';
UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1528127269322-539801943592?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'hoi-an';
UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1524231757912-21f4fe3a7200?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'hue';
UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1583212292454-1fe6229603b7?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'nha-trang';
UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1514282401047-d79a71a590e8?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'phu-quoc';
UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1472214103451-9374bd1c798e?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'da-lat';
UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'can-tho';
UPDATE destinations SET image_url = 'https://images.unsplash.com/photo-1524231757912-21f4fe3a7200?auto=format&fit=crop&w=1000&q=80' WHERE slug = 'ha-long';

SELECT slug, name, LEFT(image_url, 70) AS image_url FROM destinations ORDER BY name;
