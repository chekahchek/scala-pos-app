## Scala point of sale app
Basic CRUD app to handle inventory management

## Endpoints
| Method | URL                       | Description                          |
|--------|---------------------------|--------------------------------------|
| GET    | /quantity?inventory={name} | Returns the quantity of an inventory |
| POST   | /insert                   | Add an inventory to the database     |
| PUT    | /{name}                   | Update the inventory if it exists    |
| DELETE | /{name}                   | Delete the inventory                 |

### Example usage

- GET: `http://localhost:8080/quantity?inventory=Apple`

- POST: `curl -X POST http://localhost:8080/insert  -H "Content-Type: application/json" -d '{"name" : "Peach", "brand" : "Fruits&Co", "category" : "Fruits", "quantity": 2}'`

- PUT: `curl -X PUT http://localhost:8080/Apple  -H "Content-Type: application/json" -d '{"name" : "Apple", "brand" : "Fruits&Co", "category" : "Fruits", "quantity": 2}'`

- DELETE: `curl -X DELETE http://localhost:8080/Apple`

## References
- https://github.com/hugo-vrijswijk/todo-http4s-skunk/tree/master
- https://rockthejvm.com/articles/http4s-unleashing-the-power-of-http-apis-library
- https://rockthejvm.com/articles/mastering-skunk-the-scala-library-for-database-interaction