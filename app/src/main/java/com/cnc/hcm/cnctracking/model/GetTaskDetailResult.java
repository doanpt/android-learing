package com.cnc.hcm.cnctracking.model;

public final class GetTaskDetailResult {
    public final long statusCode;
    public final String message;
    public final Result result;

    public GetTaskDetailResult(long statusCode, String message, Result result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public static final class Result {
        public final long __v;
        public final String _id;
        public final String title;
        public final Service service;
        public final Customer customer;
        public final String note;
        public final String createdDate;
        public boolean isRead;
        public final Invoice invoice;
        public final Status status;
        public final Process process[];
        public final Executive executive[];
        public final String appointmentDate;
        public final Address address;

        public Result(String _id, String title, Service service, Customer customer, String note, long __v, String createdDate, boolean isRead, Invoice invoice, Status status, Process[] process, Executive[] executive, String appointmentDate, Address address) {
            this._id = _id;
            this.title = title;
            this.service = service;
            this.customer = customer;
            this.note = note;
            this.__v = __v;
            this.createdDate = createdDate;
            this.isRead = isRead;
            this.invoice = invoice;
            this.status = status;
            this.process = process;
            this.executive = executive;
            this.appointmentDate = appointmentDate;
            this.address = address;
        }

        public static final class Service {
            public final String _id;
            public final String name;
            public final double price;
            public final String photo;
            public final long __v;
            public final String createdDate;
            public final long tax;

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

        public static final class Customer {
            public final String _id;
            public final String type;
            public final String fullname;
            public final String phone;
            public final String email;
            public final String gender;
            public final String dateOfBirth;
            public final long __v;
            public final String createdDate;
            public final Address address;

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

            public static final class Address {
                public final String province;
                public final String district;
                public final String street;
                public final Location location;

                public Address(String province, String district, String street, Location location) {
                    this.province = province;
                    this.district = district;
                    this.street = street;
                    this.location = location;
                }

                public static final class Location {
                    public final double latitude;
                    public final double longitude;

                    public Location(double latitude, double longitude) {
                        this.latitude = latitude;
                        this.longitude = longitude;
                    }
                }
            }
        }
        public static final class Invoice {
            public final String modifiedDate;
            public final String createdDate;
            public final Status status;
            public final int discount;
            public final Process.ProcessDetail.Service[] services;
            public final Process.ProcessDetail.Product[] products;

            public Invoice(String modifiedDate, String createdDate, Status status, int discount, Process.ProcessDetail.Service[] services, Process.ProcessDetail.Product[] products) {
                this.modifiedDate = modifiedDate;
                this.createdDate = createdDate;
                this.status = status;
                this.discount = discount;
                this.services = services;
                this.products = products;
            }
        }
        public static final class Status {
            public final long _id;
            public final String title;
            public final String description;

            public Status(long _id, String title, String description) {
                this._id = _id;
                this.title = title;
                this.description = description;
            }
        }

        public static final class Process {
            public final Device device;
            public final User user;
            public final String _id;
            public final Status status;
            public final After after;
            public final ProcessDetail process;
            public final Before before;

            public Process(Device device, User user, String _id, Status status, After after, ProcessDetail process, Before before) {
                this.device = device;
                this.user = user;
                this._id = _id;
                this.status = status;
                this.after = after;
                this.process = process;
                this.before = before;
            }

            public static final class Device {
                public final String _id;
                public final Detail detail;
                public final String customer;
                public final long __v;
                public final String createdDate;

                public Device(String _id, Detail detail, String customer, long __v, String createdDate) {
                    this._id = _id;
                    this.detail = detail;
                    this.customer = customer;
                    this.__v = __v;
                    this.createdDate = createdDate;
                }

                public static final class Detail {
                    public final String _id;
                    public final Brand brand;
                    public final Category category;
                    public final String name;
                    public final String photo;
                    public final long __v;
                    public final String createdDate;

                    public Detail(String _id, Brand brand, Category category, String name, String photo, long __v, String createdDate) {
                        this._id = _id;
                        this.brand = brand;
                        this.category = category;
                        this.name = name;
                        this.photo = photo;
                        this.__v = __v;
                        this.createdDate = createdDate;
                    }

                    public static final class Brand {
                        public final String _id;
                        public final String name;
                        public final String photo;
                        public final long __v;
                        public final String createdDate;
                        public final String description;

                        public Brand(String _id, String name, String photo, long __v, String createdDate, String description) {
                            this._id = _id;
                            this.name = name;
                            this.photo = photo;
                            this.__v = __v;
                            this.createdDate = createdDate;
                            this.description = description;
                        }
                    }

                    public static final class Category {
                        public final String _id;
                        public final String title;
                        public final String photo;
                        public final long __v;
                        public final String createdDate;
                        public final String description;

                        public Category(String _id, String title, String photo, long __v, String createdDate, String description) {
                            this._id = _id;
                            this.title = title;
                            this.photo = photo;
                            this.__v = __v;
                            this.createdDate = createdDate;
                            this.description = description;
                        }
                    }

//                    public static final class Photo {
//
//                        public Photo(){
//                        }
//                    }
                }
            }

            public static final class User {
                public final String _id;
                public final String fullname;
                public final String email;
                public final String phone;
                public final long position;
                public final long skill;
                public final String photo;
                public final long __v;
                public final String dateOfBirth;
                public final String address;
                public final String createdDate;
                public final long group;
                public final boolean isDriver;
                public final String agent;

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

            public static final class Status {
                public final long _id;
                public final String title;
                public final String description;

                public Status(long _id, String title, String description) {
                    this._id = _id;
                    this.title = title;
                    this.description = description;
                }
            }

            public static final class After {
                public final String[] photos;

                public After(String[] photos) {
                    this.photos = photos;
                }
            }

            public static final class ProcessDetail {
                public final Service services[];
                public final Product products[];
                public final String[] photos;

                public ProcessDetail(Service[] services, Product[] products, String[] photos) {
                    this.services = services;
                    this.products = products;
                    this.photos = photos;
                }

                public static final class Service {
                    public final Product product;
                    public final String _id;
                    public final long quantity;

                    public Service(Product product, String _id, long quantity) {
                        this.product = product;
                        this._id = _id;
                        this.quantity = quantity;
                    }

                    public static final class Product {
                        public final String _id;
                        public final String name;
                        public final double price;
                        public final String photo;
                        public final long __v;
                        public final String createdDate;
                        public final long tax;

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

                public static final class Product {
                    public final ProductDetail product;
                    public final String _id;
                    public final long quantity;

                    public Product(ProductDetail product, String _id, long quantity) {
                        this.product = product;
                        this._id = _id;
                        this.quantity = quantity;
                    }

                    public static final class ProductDetail {
                        public final String _id;
                        public final String name;
                        public final String brand;
                        public final String category;
                        public final String photo;
                        public final long __v;
                        public final String createdDate;
                        public final long tax;
                        public final double price;
                        public final long quantity;

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

            public static final class Before {
                public final String[] photos;

                public Before(String[] photos) {
                    this.photos = photos;
                }
            }
        }

        public static final class Executive {
            public final User user;
            public final String _id;
            public final String joinedDate;
            public final Boolean isLeader;

            public Executive(User user, String _id, String joinedDate, Boolean isLeader) {
                this.user = user;
                this._id = _id;
                this.joinedDate = joinedDate;
                this.isLeader = isLeader;
            }

            public static final class User {
                public final String _id;
                public final String fullname;
                public final String email;
                public final String phone;
                public final long position;
                public final long skill;
                public final String photo;
                public final long __v;
                public final String dateOfBirth;
                public final String address;
                public final String createdDate;
                public final long group;
                public final boolean isDriver;
                public final String agent;

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

        public static final class Address {
            public final String street;
            public final String district;
            public final String province;
            public final Location location;

            public Address(String street, String district, String province, Location location) {
                this.street = street;
                this.district = district;
                this.province = province;
                this.location = location;
            }

            public static final class Location {
                public final double latitude;
                public final double longitude;

                public Location(double latitude, double longitude) {
                    this.latitude = latitude;
                    this.longitude = longitude;
                }
            }
        }
    }
}