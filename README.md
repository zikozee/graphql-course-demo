# graphql-course-demo


## Input
- Input cannot have type in them
  - hence create the Input version for the subTypes (i.e type used as field)
# graphiql  -- enable 
```yaml
spring:
  graphql:
    graphiql:
      enabled: true
```
  - ensure no trailing slash


# Pagination Relay Spec (Not Efficient for large data) 
- see image for more explanation

- GraphQl cursor connection specification
- based on relay GraphQL client (www.relay.dev)
- Terms for GraphQl pagination
  - Connection
  - Edge
  - Argument
  - PageInfo
  - Cursor

- say we have type Book
- ```graphql
    type BookConnection {
    edges: [BookEdge],
    pageInfo: PageInfo  
  }
```
- this represents a slice of book's data

- how to connect from wsl to windows auth server
-  use windows ipv4 addr