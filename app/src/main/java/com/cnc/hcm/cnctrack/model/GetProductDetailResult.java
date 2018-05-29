package com.cnc.hcm.cnctrack.model;

/**
 * Created by Android on 1/18/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductDetailResult {
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

    public class Process {

        @SerializedName("services")
        @Expose
        private List<Services.Result> services = null;
        @SerializedName("products")
        @Expose
        private List<TraddingProduct.Result> products = null;
        @SerializedName("photos")
        @Expose
        private List<String> photos = null;
        @SerializedName("note")
        @Expose
        private String note = "";

        public List<Services.Result> getServices() {
            return services;
        }

        public void setServices(List<Services.Result> services) {
            this.services = services;
        }

        public List<TraddingProduct.Result> getProducts() {
            return products;
        }

        public void setProducts(List<TraddingProduct.Result> products) {
            this.products = products;
        }

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    public class Result {

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
        private Status status;
        @SerializedName("after")
        @Expose
        private After after;
        @SerializedName("process")
        @Expose
        private Process process;
        @SerializedName("before")
        @Expose
        private Before before;

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

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public After getAfter() {
            return after;
        }

        public void setAfter(After after) {
            this.after = after;
        }

        public Process getProcess() {
            return process;
        }

        public void setProcess(Process process) {
            this.process = process;
        }

        public Before getBefore() {
            return before;
        }

        public void setBefore(Before before) {
            this.before = before;
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

    public class After {

        @SerializedName("photos")
        @Expose
        private List<String> photos = null;

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

    }

    public class Before {

        @SerializedName("photos")
        @Expose
        private List<String> photos = null;

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

    }

    public class Brand {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("description")
        @Expose
        private String description;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    public class Category {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("__v")
        @Expose
        private Long v;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("description")
        @Expose
        private String description;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    public class Detail {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("brand")
        @Expose
        private Brand brand;
        @SerializedName("category")
        @Expose
        private Category category;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("photo")
        @Expose
        private Object photo;
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

        public Brand getBrand() {
            return brand;
        }

        public void setBrand(Brand brand) {
            this.brand = brand;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getPhoto() {
            return photo;
        }

        public void setPhoto(Object photo) {
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

    }

    public class Device {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("detail")
        @Expose
        private Detail detail;
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

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
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
}
