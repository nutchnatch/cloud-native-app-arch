package org.template.demo.invoice;

import org.template.demo.address.Address;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InvoiceRepository extends
 PagingAndSortingRepository<Invoice, String> {

 Invoice findByBillingAddress(Address address);
}
