package com.cnc.hcm.cnctracking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTaskListResult {
    public long statusCode;
    public String message;
    public Result[] result;

    public GetTaskListResult(long statusCode, String message, Result[] result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public static class Result {
        public long __v;
        public String _id;
        public String title;
        public Customer customer;
        public String note;
        public String createdDate;
        public Recipient recipient;
        public boolean isRead;
        public Invoice invoice;
        public Status status;
        public Process process[];
        public Executive executive[];
        public String appointmentDate;
        public Address address;
        public RecommendedServices recommendedServices[];

        public Result(String _id, String title, Customer customer, String note, long __v, String createdDate, Recipient recipient, boolean isRead, Invoice invoice, Status status, Process[] process,
                      Executive[] executive, String appointmentDate, Address address, RecommendedServices recommendedServices[]) {
            this._id = _id;
            this.title = title;
            this.customer = customer;
            this.note = note;
            this.__v = __v;
            this.createdDate = createdDate;
            this.recipient = recipient;
            this.isRead = isRead;
            this.invoice = invoice;
            this.status = status;
            this.process = process;
            this.executive = executive;
            this.appointmentDate = appointmentDate;
            this.address = address;
            this.recommendedServices = recommendedServices;
        }

        public class RecommendedServices {

            @SerializedName("service")
            @Expose
            private Service service;
            @SerializedName("device")
            @Expose
            private String device;
            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("isDefault")
            @Expose
            private Boolean isDefault;
            @SerializedName("quantity")
            @Expose
            private Integer quantity;

            public Service getService() {
                return service;
            }

            public void setService(Service service) {
                this.service = service;
            }

            public String getDevice() {
                return device;
            }

            public void setDevice(String device) {
                this.device = device;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Boolean getIsDefault() {
                return isDefault;
            }

            public void setIsDefault(Boolean isDefault) {
                this.isDefault = isDefault;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

        }

        public static class Service {
            public String _id;
            public String name;
            public double price;
            public String photo;
            public long __v;
            public String createdDate;
            public long tax;

            public Service(String _id, String name, double price, String photo, long __v, String createdDate, long tax) {
                this._id = _id;
                this.name = name;
                this.price = price;
                this.photo = photo;
                this.__v = __v;
                this.createdDate = createdDate;
                this.tax = tax;
            }
        }

        public static class Recipient {
            private String fullname;
            private String phone;

            public Recipient(String fullname, String phone) {
                this.fullname = fullname;
                this.phone = phone;
            }

            public String getFullname() {
                return fullname;
            }
            public String getPhone() {
                return phone;
            }
        }

        public static class Customer {
            public String _id;
            public String type;
            public String fullname;
            public String phone;
            public String email;
            public String gender;
            public String dateOfBirth;
            public long __v;
            public String createdDate;
            public Address address;

            public Customer(String _id, String type, String fullname, String phone, String email, String gender, String dateOfBirth, long __v, String createdDate, Address address) {
                this._id = _id;
                this.type = type;
                this.fullname = fullname;
                this.phone = phone;
                this.email = email;
                this.gender = gender;
                this.dateOfBirth = dateOfBirth;
                this.__v = __v;
                this.createdDate = createdDate;
                this.address = address;
            }

            public static class Address {
                public String province;
                public String district;
                public String street;
                public Location location;

                public Address(String province, String district, String street, Location location) {
                    this.province = province;
                    this.district = district;
                    this.street = street;
                    this.location = location;
                }

                public static class Location {
                    public double latitude;
                    public double longitude;

                    public Location(double latitude, double longitude) {
                        this.latitude = latitude;
                        this.longitude = longitude;
                    }
                }
            }
        }
        public static class Invoice {
            public String modifiedDate;
            public String createdDate;
            public Status status;
            public int discount;
            public Process.ProcessDetail.Service[] services;
            public Product[] products;

            public Invoice(String modifiedDate, String createdDate, Status status, int discount, Process.ProcessDetail.Service[] services, Product[] products) {
                this.modifiedDate = modifiedDate;
                this.createdDate = createdDate;
                this.status = status;
                this.discount = discount;
                this.services = services;
                this.products = products;
            }

            public static class Product {
                public ProductDetail product;
                public String _id;
                public long quantity;

                public Product(ProductDetail product, String _id, long quantity) {
                    this.product = product;
                    this._id = _id;
                    this.quantity = quantity;
                }

                public static class ProductDetail {
                    public String _id;
                    public String name;
                    public Process.Device.Detail.Brand brand;
                    public String category;
                    public String photo;
                    public long __v;
                    public String createdDate;
                    public long tax;
                    public double price;
                    public long quantity;

                    public ProductDetail(String _id, String name, Process.Device.Detail.Brand brand, String category, String photo, long __v, String createdDate, long tax, double price, long quantity) {
                        this._id = _id;
                        this.name = name;
                        this.brand = brand;
                        this.category = category;
                        this.photo = photo;
                        this.__v = __v;
                        this.createdDate = createdDate;
                        this.tax = tax;
                        this.price = price;
                        this.quantity = quantity;
                    }
                }
            }
        }

        public static class Status {
            public long _id;
            public String title;
            public String description;

            public Status(long _id, String title, String description) {
                this._id = _id;
                this.title = title;
                this.description = description;
            }
        }

        public static class Process {
            public Device device;
            public User user;
            public String _id;
            public Status status;
            public After after;
            public ProcessDetail process;
            public Before before;

            public Process(Device device, User user, String _id, Status status, After after, ProcessDetail process, Before before) {
                this.device = device;
                this.user = user;
                this._id = _id;
                this.status = status;
                this.after = after;
                this.process = process;
                this.before = before;
            }

            public static class Device {
                public String _id;
                public Detail detail;
                public String customer;
                public long __v;
                public String createdDate;

                public Device(String _id, Detail detail, String customer, long __v, String createdDate) {
                    this._id = _id;
                    this.detail = detail;
                    this.customer = customer;
                    this.__v = __v;
                    this.createdDate = createdDate;
                }

                public static class Detail {
                    public String _id;
                    public Brand brand;
                    public Category category;
                    public String name;
                    public String photo;
                    public long __v;
                    public String createdDate;

                    public Detail(String _id, Brand brand, Category category, String name, String photo, long __v, String createdDate) {
                        this._id = _id;
                        this.brand = brand;
                        this.category = category;
                        this.name = name;
                        this.photo = photo;
                        this.__v = __v;
                        this.createdDate = createdDate;
                    }

                    public static class Brand {
                        public String _id;
                        public String name;
                        public String photo;
                        public long __v;
                        public String createdDate;
                        public String description;

                        public Brand(String _id, String name, String photo, long __v, String createdDate, String description) {
                            this._id = _id;
                            this.name = name;
                            this.photo = photo;
                            this.__v = __v;
                            this.createdDate = createdDate;
                            this.description = description;
                        }
                    }

                    public static class Category {
                        public String _id;
                        public String title;
                        public String photo;
                        public long __v;
                        public String createdDate;
                        public String description;

                        public Category(String _id, String title, String photo, long __v, String createdDate, String description) {
                            this._id = _id;
                            this.title = title;
                            this.photo = photo;
                            this.__v = __v;
                            this.createdDate = createdDate;
                            this.description = description;
                        }
                    }
                }
            }

            public static class User {
                public String _id;
                public String fullname;
                public String email;
                public String phone;
                public long position;
                public long skill;
                public String photo;
                public long __v;
                public String dateOfBirth;
                public String address;
                public String createdDate;
                public long group;
                public boolean isDriver;
                public String agent;

                public User(String _id, String fullname, String email, String phone, long position, long skill, String photo, long __v, String dateOfBirth, String address, String createdDate, long group, boolean isDriver, String agent) {
                    this._id = _id;
                    this.fullname = fullname;
                    this.email = email;
                    this.phone = phone;
                    this.position = position;
                    this.skill = skill;
                    this.photo = photo;
                    this.__v = __v;
                    this.dateOfBirth = dateOfBirth;
                    this.address = address;
                    this.createdDate = createdDate;
                    this.group = group;
                    this.isDriver = isDriver;
                    this.agent = agent;
                }
            }

            public static class Status {
                public long _id;
                public String title;
                public String description;

                public Status(long _id, String title, String description) {
                    this._id = _id;
                    this.title = title;
                    this.description = description;
                }
            }

            public static class After {
                public String[] photos;

                public After(String[] photos) {
                    this.photos = photos;
                }
            }

            public static class ProcessDetail {
                public Service services[];
                public Product products[];
                public String[] photos;

                public ProcessDetail(Service[] services, Product[] products, String[] photos) {
                    this.services = services;
                    this.products = products;
                    this.photos = photos;
                }

                public static class Service {
                    public Product product;
                    public String _id;
                    public long quantity;

                    public Service(Product product, String _id, long quantity) {
                        this.product = product;
                        this._id = _id;
                        this.quantity = quantity;
                    }

                    public static class Product {
                        public String _id;
                        public String name;
                        public double price;
                        public String photo;
                        public long __v;
                        public String createdDate;
                        public long tax;

                        public Product(String _id, String name, double price, String photo, long __v, String createdDate, long tax) {
                            this._id = _id;
                            this.name = name;
                            this.price = price;
                            this.photo = photo;
                            this.__v = __v;
                            this.createdDate = createdDate;
                            this.tax = tax;
                        }
                    }
                }

                public static class Product {
                    public ProductDetail product;
                    public String _id;
                    public long quantity;

                    public Product(ProductDetail product, String _id, long quantity) {
                        this.product = product;
                        this._id = _id;
                        this.quantity = quantity;
                    }

                    public static class ProductDetail {
                        public String _id;
                        public String name;
                        public String brand;
                        public String category;
                        public String photo;
                        public long __v;
                        public String createdDate;
                        public long tax;
                        public double price;
                        public long quantity;

                        public ProductDetail(String _id, String name, String brand, String category, String photo, long __v, String createdDate, long tax, double price, long quantity) {
                            this._id = _id;
                            this.name = name;
                            this.brand = brand;
                            this.category = category;
                            this.photo = photo;
                            this.__v = __v;
                            this.createdDate = createdDate;
                            this.tax = tax;
                            this.price = price;
                            this.quantity = quantity;
                        }
                    }
                }
            }

            public static class Before {
                public String[] photos;

                public Before(String[] photos) {
                    this.photos = photos;
                }
            }
        }

        public static class Executive {
            public User user;
            public String _id;
            public String joinedDate;
            public Boolean isLeader;

            public Executive(User user, String _id, String joinedDate, Boolean isLeader) {
                this.user = user;
                this._id = _id;
                this.joinedDate = joinedDate;
                this.isLeader = isLeader;
            }

            public static class User {
                public String _id;
                public String fullname;
                public String email;
                public String phone;
                public long position;
                public long skill;
                public String photo;
                public long __v;
                public String dateOfBirth;
                public String address;
                public String createdDate;
                public long group;
                public boolean isDriver;
                public String agent;

                public User(String _id, String fullname, String email, String phone, long position, long skill, String photo, long __v, String dateOfBirth, String address, String createdDate, long group, boolean isDriver, String agent) {
                    this._id = _id;
                    this.fullname = fullname;
                    this.email = email;
                    this.phone = phone;
                    this.position = position;
                    this.skill = skill;
                    this.photo = photo;
                    this.__v = __v;
                    this.dateOfBirth = dateOfBirth;
                    this.address = address;
                    this.createdDate = createdDate;
                    this.group = group;
                    this.isDriver = isDriver;
                    this.agent = agent;
                }
            }
        }

        public static class Address {
            public String street;
            public String district;
            public String province;
            public Location location;

            public Address(String street, String district, String province, Location location) {
                this.street = street;
                this.district = district;
                this.province = province;
                this.location = location;
            }

            public static class Location {
                public double latitude;
                public double longitude;

                public Location(double latitude, double longitude) {
                    this.latitude = latitude;
                    this.longitude = longitude;
                }
            }
        }
    }
}