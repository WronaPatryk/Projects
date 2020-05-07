

library(igraph)

# calculates the matrix of M nearest neighbors
Mnn <- function(X,M = (nrow(X)-1)){
  stopifnot(class(X) == "matrix" && M > 0)
  S <- matrix(Inf, nrow = nrow(X), ncol = M)
  distance <- as.matrix(dist(X, method = "euclidean", diag = NA, upper = TRUE, p = 2))
  diag(distance) <- Inf
  for (i in 1:nrow(X)){
    for (j in 1:M){ # searches for index
      S[i,j] <- which.min(distance[i,])
      distance[i, which.min(distance[i,])] <- Inf
    }
  }
  return(S)
}

# Creates an undirected graph - as neighborhood matrix 
Mnn_graph <- function(S){
  stopifnot(class(S) == "matrix")
  N <- nrow(S)
  G <- matrix(0, nrow = N, ncol = N)
  for (i in 1:N){
    for(j in 1:N){
      for(u in 1:ncol(S)){
        if(S[i,u] == j || S[j,u] == i){
          G[i,j] <- 1
        }
      }
    }
  }
  # get the components
  res <- components(graph_from_adjacency_matrix(G, mode = 'undirected'))$membership
  G.. <- split(1:length(res), res)
  length(G..) -> numberOfComponents
  
  # checks if the graph is not connected:
  if ( numberOfComponents > 1){
    # if it is not connected, makes it connected:
    # (!) note that there can be numerous 'connecting' techniques
    # -> I take THE LAST vertex of each component 
    # -> and connect it to THE FIRST vertex of the next component
    for(m in 1:(numberOfComponents - 1)){
      G[[G..[[m]][length(G..[[m]])], G..[[m+1]][1] ]] <- 1
      G[[G..[[m+1]][1], G..[[m]][length(G..[[m]])] ]] <- 1
    }
  }
  return(G)
}

# calculates the laplasian of the connected graph (n. matrix)
# then returns the matrix of its eigen vectors as columns
Laplacian_eigen <- function(G,k){
  stopifnot(k < nrow(G) && k > 1)
  # the formula is:  L = D - G, 
  # where D is a diagonal matrix such that D(i,i) = degree of i-th vertex
  D <- degree(graph_from_adjacency_matrix(G))
  G <- -G #  saving memory
  for (i in 1:nrow(G)){
    G[i,i] <- D[i]
  }
  
  # gets the eigen vectors:
  # by default the eigen values are in decreasing order 
  # ... and so are their corresponding eigen vectors
  ev <- eigen(G)
  vectors <- ev$vectors
  # gets the eigen vectors corresponding to the first k smallest eigen values:
  E <- matrix(NA, nrow = nrow(G), ncol = k)
  for (i in 1:k){
    E[,i] <- vectors[,nrow(G)-i] # takes into account decreasing order
  }
  return(E)
}

# now we only need to use kmeans() on the result:

# INPUT:
# X - matrix
# k - number of clusters
# M - number of nearest neighbors

# OUTPUT:
# vector - division of the observations into k classes

spectral_clustering <- function(X, k, M){
  M_nearest_neighbours_matrix <- Mnn(X, M)
  connected_graph <- Mnn_graph(M_nearest_neighbours_matrix)
  matrix_of_eigenVectors <- Laplacian_eigen(connected_graph, k)
  
  # using kmeans with k clusters and 20 aleatory attempts:
  return(kmeans(matrix_of_eigenVectors, 
                centers = k, nstart = 20)$cluster)
}
