package com.example.demo001.domain.OrderManagement;

/** Enumeration of all possible states of the order */
public enum OrderStatus {
    /** Order placed in the system by client */
    ISSUED,
    /** Order paired with factory, looking for TransportProvider */
    LKN_4_TP,
    /** Order paired with factory and waiting for acceptation from TransportProvider */
    PENDING_TP,
    /** Order paired with factories and delivery by order manager and sent to client for acceptation */
    OFFER_SENT,
    /** Order accepted by client and sent to realization */
    ACCEPTED,
    /** Order rejected by client and dropped from realization */
    REJECTED,
    /** Order ready for transport */
    COMPLETED,
    /** Order is being transported */
    IN_TRANSPORT,
    /** Order delivery is completed */
    DELIVERED
}
/** Klient wysyła zamówienie -> OrderManager wybiera fabryki i TransportProvidera
 * -> TransportProvider akceptuje lub nie akceptuje  przewozu
 *   -> transport zaakaceptowany -> oferta idzie do akceptacji lub odrzucenia przez klienta
 *   -> transport odrzucony -> oferta wraca do OrderManagera, który musi wybrać inny transport
 *
 * -> zamówienie zaakceptowane -> zamówienie gotowe do wysyłki
 * -> zamówienie w transporcie -> zamówienie dostarczone.
 */