package com.cnc.hcm.cnctracking.model;

public final class ProcessDeviceResult {
    public final long statusCode;
    public final String message;
    public final Result result;

    public ProcessDeviceResult(long statusCode, String message, Result result){
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public static final class Result {
        public final String _id;
        public final String title;
        public final String service;
        public final String customer;
        public final String note;
        public final long __v;
        public final String createdDate;
        public final long status;
        public final Proces process[];
        public final Executive executive[];
        public final String appointmentDate;
        public final Address address;

        public Result(String _id, String title, String service, String customer, String note, long __v, String createdDate, long status, Proces[] process, Executive[] executive, String appointmentDate, Address address){
            this._id = _id;
            this.title = title;
            this.service = service;
            this.customer = customer;
            this.note = note;
            this.__v = __v;
            this.createdDate = createdDate;
            this.status = status;
            this.process = process;
            this.executive = executive;
            this.appointmentDate = appointmentDate;
            this.address = address;
        }

        public static final class Proces {
            public final String device;
            public final String user;
            public final String _id;
            public final long status;
            public final After after;
            public final Process process;
            public final Before before;

            public Proces(String device, String user, String _id, long status, After after, Process process, Before before){
                this.device = device;
                this.user = user;
                this._id = _id;
                this.status = status;
                this.after = after;
                this.process = process;
                this.before = before;
            }

            public static final class After {
                public final String[] photos;

                public After(String[] photos){
                    this.photos = photos;
                }
            }

            public static final class Process {
                public final Service services[];
                public final Product products[];
                public final String[] photos;

                public Process(Service[] services, Product[] products, String[] photos){
                    this.services = services;
                    this.products = products;
                    this.photos = photos;
                }

                public static final class Service {
                    public final String product;
                    public final String _id;
                    public final long quantity;

                    public Service(String product, String _id, long quantity){
                        this.product = product;
                        this._id = _id;
                        this.quantity = quantity;
                    }
                }

                public static final class Product {
                    public final String product;
                    public final String _id;
                    public final long quantity;

                    public Product(String product, String _id, long quantity){
                        this.product = product;
                        this._id = _id;
                        this.quantity = quantity;
                    }
                }
            }

            public static final class Before {
                public final String[] photos;

                public Before(String[] photos){
                    this.photos = photos;
                }
            }
        }

        public static final class Executive {
            public final String user;
            public final String _id;
            public final String joinedDate;
            public final Boolean isLeader;

            public Executive(String user, String _id, String joinedDate, Boolean isLeader){
                this.user = user;
                this._id = _id;
                this.joinedDate = joinedDate;
                this.isLeader = isLeader;
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