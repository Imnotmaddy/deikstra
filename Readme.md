Run maven install and start the application.
Application uses in-memory database, so no explicit DB configuration is required.

*Bullet decisions*:
1. As it was not specified in the task, cities have only one-directional paths;
2. To ease start of the application in-memory DB is used.
3. @Version annotation is used in Route entity for concurrency issues. If two threads try to write to DB in the same time
one of them will fail with OptimisticLockException which is handled in controller.
4. To perform search RouteService reads data from DB only once and never interacts with DB afterwards so there are no concurrency issues in that regard.
5. With every new request for finding paths between cities RouteService rebuilds connections between cities and feeds it to 
PathFinder. Thus user is guaranteed to receive a result based on data he had at the moment of his request.

6. PathFinder receives only starting city/node for search. Since node contains a tree of all nodes it can reach,
search algorithm doesn't go through separated or secluded trees.
7. I used Interfaces for my services to make them interchangeable if required. 
Testing it!