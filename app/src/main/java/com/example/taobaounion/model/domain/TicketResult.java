package com.example.taobaounion.model.domain;

public class TicketResult {

    private Boolean success;
    private Integer code;
    private String message;
    private DataBean data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TicketResult{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        private TbkTpwdCreateResponseDTO tbk_tpwd_create_response;

        public TbkTpwdCreateResponseDTO getTbk_tpwd_create_response() {
            return tbk_tpwd_create_response;
        }

        public void setTbk_tpwd_create_response(TbkTpwdCreateResponseDTO tbk_tpwd_create_response) {
            this.tbk_tpwd_create_response = tbk_tpwd_create_response;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "tbk_tpwd_create_response=" + tbk_tpwd_create_response +
                    '}';
        }

        public static class TbkTpwdCreateResponseDTO {
            private DataDTO data;
            private String request_id;

            public DataDTO getData() {
                return data;
            }

            public void setData(DataDTO data) {
                this.data = data;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            @Override
            public String toString() {
                return "TbkTpwdCreateResponseDTO{" +
                        "data=" + data +
                        ", request_id='" + request_id + '\'' +
                        '}';
            }

            public static class DataDTO {
                private String model;

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
                }

                @Override
                public String toString() {
                    return "DataDTO{" +
                            "model='" + model + '\'' +
                            '}';
                }
            }
        }
    }
}
