package com.cnc.hcm.cnctracking.model;

/**
 * Created by Android on 1/11/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailResult {

    @SerializedName("statusCode")
    @Expose
    private Long statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Result result;

    public Long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("service")
        @Expose
        private Service service;
        @SerializedName("customer")
        @Expose
        private Customer customer;
        @SerializedName("note")
        @Expose
        private String note;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("status")
        @Expose
        private Status status;
        @SerializedName("process")
        @Expose
        private List<Process> process = null;
        @SerializedName("executive")
        @Expose
        private List<Executive> executive = null;
        @SerializedName("appointmentDate")
        @Expose
        private String appointmentDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public List<Process> getProcess() {
            return process;
        }

        public void setProcess(List<Process> process) {
            this.process = process;
        }

        public List<Executive> getExecutive() {
            return executive;
        }

        public void setExecutive(List<Executive> executive) {
            this.executive = executive;
        }

        public String getAppointmentDate() {
            return appointmentDate;
        }

        public void setAppointmentDate(String appointmentDate) {
            this.appointmentDate = appointmentDate;
        }

    }

    public class Address {

        @SerializedName("province")
        @Expose
        private String province;
        @SerializedName("district")
        @Expose
        private String district;
        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("location")
        @Expose
        private Location location;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

    }

    public class Customer {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("dateOfBirth")
        @Expose
        private String dateOfBirth;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("address")
        @Expose
        private Address address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

    }

    public class Device {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("detail")
        @Expose
        private String detail;
        @SerializedName("customer")
        @Expose
        private String customer;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

    }

    public class Executive {

        @SerializedName("user")
        @Expose
        private User_ user;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("joinedDate")
        @Expose
        private String joinedDate;

        public User_ getUser() {
            return user;
        }

        public void setUser(User_ user) {
            this.user = user;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJoinedDate() {
            return joinedDate;
        }

        public void setJoinedDate(String joinedDate) {
            this.joinedDate = joinedDate;
        }

    }

    public class Location {

        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("longitude")
        @Expose
        private Double longitude;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

    }

    public class Process {

        @SerializedName("device")
        @Expose
        private Device device;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("status")
        @Expose
        private Status_ status;
        @SerializedName("after")
        @Expose
        private List<Object> after = null;
        @SerializedName("process")
        @Expose
        private Process_ process;
        @SerializedName("before")
        @Expose
        private List<Object> before = null;

        public Device getDevice() {
            return device;
        }

        public void setDevice(Device device) {
            this.device = device;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Status_ getStatus() {
            return status;
        }

        public void setStatus(Status_ status) {
            this.status = status;
        }

        public List<Object> getAfter() {
            return after;
        }

        public void setAfter(List<Object> after) {
            this.after = after;
        }

        public Process_ getProcess() {
            return process;
        }

        public void setProcess(Process_ process) {
            this.process = process;
        }

        public List<Object> getBefore() {
            return before;
        }

        public void setBefore(List<Object> before) {
            this.before = before;
        }

    }

    public class Process_ {

        @SerializedName("products")
        @Expose
        private List<Object> products = null;
        @SerializedName("photos")
        @Expose
        private List<Object> photos = null;

        public List<Object> getProducts() {
            return products;
        }

        public void setProducts(List<Object> products) {
            this.products = products;
        }

        public List<Object> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Object> photos) {
            this.photos = photos;
        }

    }

    public class Service {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("tax")
        @Expose
        private Long tax;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Long getTax() {
            return tax;
        }

        public void setTax(Long tax) {
            this.tax = tax;
        }

    }

    public class Status {

        @SerializedName("_id")
        @Expose
        private Long id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    public class Status_ {

        @SerializedName("_id")
        @Expose
        private Long id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("description")
        @Expose
        private String description;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    public class User {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("position")
        @Expose
        private Long position;
        @SerializedName("skill")
        @Expose
        private Long skill;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("accessToken")
        @Expose
        private String accessToken;
        @SerializedName("dateOfBirth")
        @Expose
        private String dateOfBirth;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("group")
        @Expose
        private Long group;
        @SerializedName("isDriver")
        @Expose
        private Boolean isDriver;
        @SerializedName("agent")
        @Expose
        private String agent;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Long getPosition() {
            return position;
        }

        public void setPosition(Long position) {
            this.position = position;
        }

        public Long getSkill() {
            return skill;
        }

        public void setSkill(Long skill) {
            this.skill = skill;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Long getGroup() {
            return group;
        }

        public void setGroup(Long group) {
            this.group = group;
        }

        public Boolean getIsDriver() {
            return isDriver;
        }

        public void setIsDriver(Boolean isDriver) {
            this.isDriver = isDriver;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

    }

    public class User_ {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("position")
        @Expose
        private Long position;
        @SerializedName("skill")
        @Expose
        private Long skill;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("accessToken")
        @Expose
        private String accessToken;
        @SerializedName("dateOfBirth")
        @Expose
        private String dateOfBirth;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("group")
        @Expose
        private Long group;
        @SerializedName("isDriver")
        @Expose
        private Boolean isDriver;
        @SerializedName("agent")
        @Expose
        private String agent;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Long getPosition() {
            return position;
        }

        public void setPosition(Long position) {
            this.position = position;
        }

        public Long getSkill() {
            return skill;
        }

        public void setSkill(Long skill) {
            this.skill = skill;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Long getGroup() {
            return group;
        }

        public void setGroup(Long group) {
            this.group = group;
        }

        public Boolean getIsDriver() {
            return isDriver;
        }

        public void setIsDriver(Boolean isDriver) {
            this.isDriver = isDriver;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

    }
}