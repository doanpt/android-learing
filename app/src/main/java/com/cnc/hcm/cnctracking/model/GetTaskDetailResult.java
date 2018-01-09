package com.cnc.hcm.cnctracking.model;

public final class GetTaskDetailResult {
    public final long statusCode;
    public final String message;
    public final Result result;

    public GetTaskDetailResult(long statusCode, String message, Result result){
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public static final class Result {
        public final String _id;
        public final String title;
        public final Service service;
        public final Customer customer;
        public final String note;
        public final String id;
        public final long __v;
        public final String createdDate;
        public final Status status;
        public final Process[] process;
        public final Executive[] executive;
        public final String appointmentDate;
        public final Address address;

        public Result(String _id, String title, Service service, Customer customer, String note, String id, long __v, String createdDate, Status status, Process[] process, Executive[] executive, String appointmentDate, Address address){
            this._id = _id;
            this.title = title;
            this.service = service;
            this.customer = customer;
            this.note = note;
            this.id = id;
            this.__v = __v;
            this.createdDate = createdDate;
            this.status = status;
            this.process = process;
            this.executive = executive;
            this.appointmentDate = appointmentDate;
            this.address = address;
        }

        public static final class Service {
            public final String _id;
            public final String name;
            public final String price;
            public final String photo;
            public final long __v;
            public final String createdDate;
            public final long tax;

            public Service(String _id, String name, String price, String photo, long __v, String createdDate, long tax){
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

            public Customer(String _id, String type, String fullname, String phone, String email, String gender, String dateOfBirth, long __v, String createdDate, Address address){
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

                public Address(String province, String district, String street, Location location){
                    this.province = province;
                    this.district = district;
                    this.street = street;
                    this.location = location;
                }

                public static final class Location {
                    public final double latitude;
                    public final double longitude;

                    public Location(double latitude, double longitude){
                        this.latitude = latitude;
                        this.longitude = longitude;
                    }
                }
            }
        }

        public static final class Status {
            public final long _id;
            public final String title;
            public final String description;

            public Status(long _id, String title, String description){
                this._id = _id;
                this.title = title;
                this.description = description;
            }
        }

        public static final class Process {
            public final String user;
            public final String device;
            public final String before;
            public final ProcessDetail process;
            public final String after;
            public final int status;
            public final String date;
            public final Location location;

            public Process(String user, String device, String before, ProcessDetail process, String after, int status, String date, Location location) {
                this.user = user;
                this.device = device;
                this.before = before;
                this.process = process;
                this.after = after;
                this.status = status;
                this.date = date;
                this.location = location;
            }

            public static final class ProcessDetail {
                public final String photos;
                public final Product[] products;

                public ProcessDetail(String photos, Product[] products) {
                    this.photos = photos;
                    this.products = products;
                }

                public static final class Product {
                    public final String product;
                    public final int quantity;

                    public Product(String product, int quantity){
                        this.product = product;
                        this.quantity = quantity;
                    }
                }
            }

            public static final class Location {
                public final double latitude;
                public final double longitude;

                public Location(double latitude, double longitude){
                    this.latitude = latitude;
                    this.longitude = longitude;
                }
            }
        }


        public static final class Executive {
            public final User user;
            public final String _id;
            public final String joinedDate;

            public Executive(User user, String _id, String joinedDate){
                this.user = user;
                this._id = _id;
                this.joinedDate = joinedDate;
            }

            public static final class User {
                public final String _id;
                public final String fullname;
                public final String password;
                public final String email;
                public final String dateOfBirth;
                public final String address;
                public final String phone;
                public final long position;
                public final long skill;
                public final String photo;
                public final long __v;
                public final String accessToken;
                public final String createdDate;
                public final long group;
                public final boolean isDriver;
                public final String agent;

                public User(String _id, String fullname, String password, String email, String dateOfBirth, String address, String phone, long position, long skill, String photo, long __v, String accessToken, String createdDate, long group, boolean isDriver, String agent){
                    this._id = _id;
                    this.fullname = fullname;
                    this.password = password;
                    this.email = email;
                    this.dateOfBirth = dateOfBirth;
                    this.address = address;
                    this.phone = phone;
                    this.position = position;
                    this.skill = skill;
                    this.photo = photo;
                    this.__v = __v;
                    this.accessToken = accessToken;
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

            public Address(String street, String district, String province, Location location){
                this.street = street;
                this.district = district;
                this.province = province;
                this.location = location;
            }

            public static final class Location {
                public final double latitude;
                public final double longitude;

                public Location(double latitude, double longitude){
                    this.latitude = latitude;
                    this.longitude = longitude;
                }
            }
        }
    }
}