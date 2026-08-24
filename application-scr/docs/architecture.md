# ShopSphere Phase 2 architecture

```text
                    +--------------------+
                    | React Frontend     |
                    | localhost:5173     |
                    +----------+---------+
                               |
             +-----------------+------------------+
             |        |             |              |
             v        v             v              v
        Product    User         Order          Payment
        :8081      :8082        :8083           :8084
          |          |             |              |
          +----------+-------------+              |
                     |                            |
                     v                            v
               PostgreSQL                    Stateless mock
                shopsphere                    payment API
```

Later DevOps phases will place these services behind Kubernetes Services/Ingress, connect PostgreSQL to Cloud SQL, build/push images to Artifact Registry, and automate delivery with Jenkins/Argo CD.
