package com.example.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClientDto {

    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @Pattern(regexp = "^(\\+27|0)[1-9]\\d{8}$", message = "Invalid mobile number")
    private String mobileNumber;
    @NotBlank(message = "ID number is mandatory")
    @Pattern(regexp = "^\\d{13}$", message = "Invalid ID number")
    private String idNumber;
    private String physicalAddress;

    public ClientDto() {
    }

    public ClientDto(String firstName, String lastName, String mobileNumber, String idNumber, String physicalAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.idNumber = idNumber;
        this.physicalAddress = physicalAddress;
    }



    // Getters and setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public static ClientDto from(Client client) {
        return new ClientDto(
                client.getFirstName(),
                client.getLastName(),
                client.getMobileNumber(),
                client.getIdNumber(),
                client.getPhysicalAddress()
        );
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ClientDto)) {
            return false;
        }

        ClientDto other = (ClientDto) obj;

        return Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName) &&
                Objects.equals(mobileNumber, other.mobileNumber) &&
                Objects.equals(idNumber, other.idNumber) &&
                Objects.equals(physicalAddress, other.physicalAddress);
    }


    // toString method

    @Override
    public String toString() {
        return "ClientDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", physicalAddress='" + physicalAddress + '\'' +
                '}';
    }

}
