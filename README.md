# need to import json-simple-1.1.1.jar
# the main method is int src/venmo/Main.java
# 
# Program Hierarchy Chart
#
#                                                                    Main
#                                                                      |
#                                                                      |
#                                                         UpdatePaymentsInTimeWindow (read each payment, output median degree and update #                                                         Venmo graph)
#                                                                      |
#                                                                      |
#                               ------------------------------------------------------------------------------
#                               |                                      |                                     |
#                               |                                      |                                     |
#                          MedianDegree(update median degree)       Payment (data class               DrawVenmoGraph (draw Venmo graph)
#                               |                                   records names + payment time stamp)      |
#                               |                                                                            |
#         -------------------------------------------                                                   Coordinate (data class records
#         |                     |                   |                                                   location on graph)
#         |                     |                   |
# MinPriorityQueue      MaxPriorityQueue      VerticeDegree (data class records name and degree on graph)
#
#
