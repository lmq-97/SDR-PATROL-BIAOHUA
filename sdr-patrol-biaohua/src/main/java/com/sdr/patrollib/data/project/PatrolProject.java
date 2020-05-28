package com.sdr.patrollib.data.project;

import com.sdr.patrollib.data.MetaDataInfo;

import java.util.List;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description:
 */

public class PatrolProject extends MetaDataInfo {
    /**
     * 主键(自增)
     */
    private int id;

    /**
     * 巡检工程的ID
     */
    private String projectId;

    /**
     * 巡检项目（工程）名称
     * varchar(50)
     */
    private String projectName;

    /**
     * 巡检项目（工程）位置：经度
     */
    private double lng;

    /**
     * 巡检项目（工程）位置：纬度
     */
    private double lat;

    /**
     * 巡检项目（工程）类型
     * varchar(20)
     */
    private String type;

    /**
     * 所在地区
     * varchar(100)
     */
    private String address;

    /**
     * 备注
     * varchar(200)
     */
    private String remarks;


    //region 巡查频次设置
    /**
     * 巡检频率类型（周、月）
     */
    private String frequencyType;

    /**
     * 巡检频次设置
     */
    private int frequencyCount;
    //endregion


    //region 通过触发器更新，在保存设备巡检结果时
    /**
     * 巡检次数
     */
    private int count;

    /**
     * 最后一次巡检时间
     */
    private long lastPatrolTime;

    /**
     * 最后一次巡检人ID，参照：员工信息表
     */
    private String lastPatrolEmployeeId;

    /**
     * 最后一次巡检人姓名，参照：员工信息表
     * varchar(50)
     */
    private String lastPatrolEmployeeName;
    //endregion

    private List<PatrolMobileCheckItemsVo> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public int getFrequencyCount() {
        return frequencyCount;
    }

    public void setFrequencyCount(int frequencyCount) {
        this.frequencyCount = frequencyCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getLastPatrolTime() {
        return lastPatrolTime;
    }

    public void setLastPatrolTime(long lastPatrolTime) {
        this.lastPatrolTime = lastPatrolTime;
    }

    public String getLastPatrolEmployeeId() {
        return lastPatrolEmployeeId;
    }

    public void setLastPatrolEmployeeId(String lastPatrolEmployeeId) {
        this.lastPatrolEmployeeId = lastPatrolEmployeeId;
    }

    public String getLastPatrolEmployeeName() {
        return lastPatrolEmployeeName;
    }

    public void setLastPatrolEmployeeName(String lastPatrolEmployeeName) {
        this.lastPatrolEmployeeName = lastPatrolEmployeeName;
    }

    public List<PatrolMobileCheckItemsVo> getItems() {
        return items;
    }

    public void setItems(List<PatrolMobileCheckItemsVo> items) {
        this.items = items;
    }

    public static class PatrolMobileCheckItemsVo extends MetaDataInfo {
        /**
         * 主键(自增)
         */
        private int id;

        /**
         * 外键，参照：主表 Patrol_MobileCheck
         */
        private int mobileCheckId;

        /**
         * 父级ID，第一级：0，第二季：指向父ID
         */
        private int parentId;
        /**
         * 指标项名称
         */
        private String title;
        /**
         * 工程名称
         * varchar(50)
         */
        private String name;

        /**
         * 指标项用的图片，推荐：64Px * 64Px
         */
        private String imgUri;

        private List<Patrol_MobileCheckItems> items;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMobileCheckId() {
            return mobileCheckId;
        }

        public void setMobileCheckId(int mobileCheckId) {
            this.mobileCheckId = mobileCheckId;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUri() {
            return imgUri;
        }

        public void setImgUri(String imgUri) {
            this.imgUri = imgUri;
        }

        public List<Patrol_MobileCheckItems> getItems() {
            return items;
        }

        public void setItems(List<Patrol_MobileCheckItems> items) {
            this.items = items;
        }

        public static class Patrol_MobileCheckItems extends MetaDataInfo {
            /**
             * 主键(自增)
             */
            private int id;

            /**
             * 外键，参照：主表 Patrol_MobileCheck
             */
            private int mobileCheckId;

            /**
             * 父级ID，第一级：0，第二季：指向父ID
             */
            private int parentId;
            /**
             * 指标项名称
             */
            private String title;
            /**
             * 工程名称
             * varchar(50)
             */
            private String name;

            /**
             * 指标项用的图片，推荐：64Px * 64Px
             */
            private String imgUri;


            //region 曾经该巡检项发现过缺陷，需在今后的巡检过程中做"日常跟踪"处理

            /**
             * 隐患ID，外键，参照：Maintenance_DefectInfo
             */
            private String defectId;

            /**
             * 隐患名称(组成：场所（具体位置、问题分类等）)
             * varchar(200)
             */
            private String defectSubject;

            /**
             * 隐患描述
             * varchar(1000)
             */
            private String defectContentDesc;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMobileCheckId() {
                return mobileCheckId;
            }

            public void setMobileCheckId(int mobileCheckId) {
                this.mobileCheckId = mobileCheckId;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImgUri() {
                return imgUri;
            }

            public void setImgUri(String imgUri) {
                this.imgUri = imgUri;
            }

            public String getDefectId() {
                return defectId;
            }

            public void setDefectId(String defectId) {
                this.defectId = defectId;
            }

            public String getDefectSubject() {
                return defectSubject;
            }

            public void setDefectSubject(String defectSubject) {
                this.defectSubject = defectSubject;
            }

            public String getDefectContentDesc() {
                return defectContentDesc;
            }

            public void setDefectContentDesc(String defectContentDesc) {
                this.defectContentDesc = defectContentDesc;
            }
        }
    }
}
