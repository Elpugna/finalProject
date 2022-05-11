package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.Address;
import com.applaudostudios.resourceserver.model.Customer;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("The Addresses Repository: ")
class AddressRepositoryTest extends BaseContainer {
  @Autowired
  private AddressRepository addressRepository;
  @Autowired
  private CustomerRepository customerRepository;

  AddressRepositoryTest() {
  }

  @Test
  @DisplayName("Should return the list of addresses asociated with a given customer")
  void findByCustomer() {
    Customer customer = (Customer)this.customerRepository.getById(1L);
    List<Address> addressList = this.addressRepository.findByCustomer(customer);
    MatcherAssert.assertThat(addressList.size(), Matchers.is(Matchers.equalTo(2)));
    MatcherAssert.assertThat(((Address)addressList.get(0)).getAddress(), Matchers.is(Matchers.equalTo("221B Baker Street")));
    MatcherAssert.assertThat(((Address)addressList.get(1)).getAddress(), Matchers.is(Matchers.equalTo("Privet Drive 4")));
  }

  @Test
  @DisplayName("Should return an empty list when customer does not have associated addresses.")
  void ShouldNotFindByEmptyCustomer() {
    Customer customer = (Customer)this.customerRepository.getById(3L);
    List<Address> addressList = this.addressRepository.findByCustomer(customer);
    MatcherAssert.assertThat(addressList.isEmpty(), Matchers.is(Matchers.equalTo(true)));
  }
}
