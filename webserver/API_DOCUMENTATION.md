## API Documentation

### *Get Places List*

*Returns json data with all the places.*

* **URL:** `/ws/places`

* **Method:** `GET`

* **Success Response:**

  * *Code:* `200`
  * *Content:*

  ```json
  [
     {
       "id":"87beb070-94d1-4c81-98da-d58fab1727f6",
       "label":"Manhattan, NY",
       "country":"United States of America",
       "image_id":"9b6c30e1-d4a2-4302-9306-f3db5bd5f90a",
       "description":"Manhattan is the most densely populated borough of New York City, its economic and administrative center, and the city's historical birthplace."
     },
     {
       "id":"78f8bc4c-f992-48bc-afde-71b8d80eeb7b",
       "label":"Manarola, Cinque Terre",
       "country":"Italy",
       "image_id":"4b950a69-b225-4b34-9860-5816b57fe2a0",
       "description":"The Cinque Terre is a rugged portion of coast on the Italian Riviera. It is in the Liguria region of Italy, to the west of the city of La Spezia."
     }
  ]
  ```

* **Error Response:**

  * *Code:* `500 Internal Server Error`
  * *Content:*

  ```json
  { "error" : "Internal Server Error" }
  ```

### *Get Place Details*

*Returns json data with one place details.*

* **URL:** `/ws/places/:placeId`

* **Method:** `GET`

*  **URL Params**

   **Required:** `placeId=[string]`

* **Success Response:**

  * *Code:* `200`
  * *Content:*

  ```json
  {
      "id":"87beb070-94d1-4c81-98da-d58fab1727f6",
      "label":"Manhattan, NY",
      "country":"United States of America",
      "image_id":"9b6c30e1-d4a2-4302-9306-f3db5bd5f90a",
      "image_author":"Michael Muraz",
      "image_credit":"https://plus.google.com/+MichaelMuraz",
      "description":"Manhattan is the most densely populated borough of New York City, its economic and administrative center, and the city's historical birthplace. The borough is coextensive with New York County, founded on November 1, 1683, as one of the original counties of the U.S. state of New York. The borough consists mostly of Manhattan Island, bounded by the East, Hudson, and Harlem Rivers, and also includes several small adjacent islands and Marble Hill, a small neighborhood on the U.S. mainland.",
      "latitiude":40.790278,
      "longitude":-73.959722
  }
  ```

* **Error Response:**

  * *Code:* `500 Internal Server Error`
  * *Content:*

  ```json
  { "error" : "Internal Server Error" }
  ```

### *Get Image*

*Returns a .jpg file.*

* **URL:** `/images/:imageId`

* **Method:** `GET`

*  **URL Params**

   **Required:** `imageId=[string]`

* **Success Response:**

  * *Code:* `200`
  * *Content:* image file

* **Error Response:**

  * *Code:* `404 Not Found`
  * *Content:*

  ```json
  { "error" : "Image Not Found" }
  ```

