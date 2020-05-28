package com.sdr.patrollib.data.device;

import com.sdr.patrollib.data.MetaDataInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 设备巡检：巡检项目
 * Created by wan on 2018/7/16.
 */

public class PatrolDevice extends MetaDataInfo {
    /**
     * 主键(自增)
     */
    private int id;

    /**
     * 巡检工程的ID
     * 20181219 add
     */
    private String projectId;

    /**
     * 巡检工程的名称
     * 20181219 add
     */
    private String projectName;

    /**
     * 外键：设备信息
     * varchar(50)
     * 只关联设备信息中设置成 可否与外部系统关联 的设备
     */
    private String deviceId;

    /**
     * 巡检设备名称
     */
    private String deviceName;

    /**
     * 近场检测标志类型
     * NFC、WiFi、QRCode、等，参照：Patrol_MarkTypeEnum
     */
    private String markType;

    /**
     * 近场检测标志code
     */
    private String markCode;


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
     */
    private String lastPatrolEmployeeName;
    //endregion

    /**
     * 设备巡检要求的pdf
     */
    private String pdfUri;

    private List<PatrolFacilityCheckItemsVo> patrolFacilityCheckItemsVos;

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMarkType() {
        return markType;
    }

    public void setMarkType(String markType) {
        this.markType = markType;
    }

    public String getMarkCode() {
        return markCode;
    }

    public void setMarkCode(String markCode) {
        this.markCode = markCode;
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

    public List<PatrolFacilityCheckItemsVo> getPatrolFacilityCheckItemsVos() {
        return patrolFacilityCheckItemsVos;
    }

    public void setPatrolFacilityCheckItemsVos(List<PatrolFacilityCheckItemsVo> patrolFacilityCheckItemsVos) {
        this.patrolFacilityCheckItemsVos = patrolFacilityCheckItemsVos;
    }

    public String getPdfUri() {
        return pdfUri;
    }

    public void setPdfUri(String pdfUri) {
        this.pdfUri = pdfUri;
    }


    public static class PatrolFacilityCheckItemsVo extends MetaDataInfo {

        /**
         * 主键(自增)
         */
        private int id;

        /**
         * 外键，参考：主表 Patrol_FacilityCheck
         */
        private int facilityCheckId;
        /**
         * 指标项名称
         */
        private String title;
        /**
         * 指标项内容
         */
        private String name;

        /**
         * 指标项用的图片，推荐：64Px * 64Px
         */
        private String imgUri;

        private List<Patrol_FacilityCheckItemContents> patrolFacilityCheckItemContents;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFacilityCheckId() {
            return facilityCheckId;
        }

        public void setFacilityCheckId(int facilityCheckId) {
            this.facilityCheckId = facilityCheckId;
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

        public List<Patrol_FacilityCheckItemContents> getPatrolFacilityCheckItemContents() {
            return patrolFacilityCheckItemContents;
        }

        public void setPatrolFacilityCheckItemContents(List<Patrol_FacilityCheckItemContents> patrolFacilityCheckItemContents) {
            this.patrolFacilityCheckItemContents = patrolFacilityCheckItemContents;
        }

        public static class Patrol_FacilityCheckItemContents implements Serializable {

            /**
             * 主键(自增)
             */
            private int id;

            /**
             * 外键，参照：主表 Patrol_FacilityCheckItems
             */
            private int facilityCheckItemId;

            /**
             * 检查项类别（检查项、抄表项），参照：Patrol_CheckTypeEnum
             */
            private String checkType;

            /**
             * 检查／抄表项名称
             */
            private String checkName;

            /**
             * 检查／抄表项内容
             */
            private String checkContent;

            /**
             * 抄表项：输入数据类型，如：radio、checkbox、number、String，参照：Patrol_MeterReadingTypeEnum
             */
            private String meterReadingType;

            /**
             * 可选项列表（输入数据类型为单选、多选时使用）
             * 多个选项间用逗号分隔
             * varchar(500)
             */
            private String meterOptions;

            /**
             * 正常值范围下限值
             */
            private double meterLowerLimit;

            /**
             * 正常值范围上限值
             */
            private double meterUpperLimit;


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

            public int getFacilityCheckItemId() {
                return facilityCheckItemId;
            }

            public void setFacilityCheckItemId(int facilityCheckItemId) {
                this.facilityCheckItemId = facilityCheckItemId;
            }

            public String getCheckType() {
                return checkType;
            }

            public void setCheckType(String checkType) {
                this.checkType = checkType;
            }

            public String getCheckName() {
                return checkName;
            }

            public void setCheckName(String checkName) {
                this.checkName = checkName;
            }

            public String getCheckContent() {
                return checkContent;
            }

            public void setCheckContent(String checkContent) {
                this.checkContent = checkContent;
            }

            public String getMeterReadingType() {
                return meterReadingType;
            }

            public void setMeterReadingType(String meterReadingType) {
                this.meterReadingType = meterReadingType;
            }

            public String getMeterOptions() {
                return meterOptions;
            }

            public void setMeterOptions(String meterOptions) {
                this.meterOptions = meterOptions;
            }

            public double getMeterLowerLimit() {
                return meterLowerLimit;
            }

            public void setMeterLowerLimit(double meterLowerLimit) {
                this.meterLowerLimit = meterLowerLimit;
            }

            public double getMeterUpperLimit() {
                return meterUpperLimit;
            }

            public void setMeterUpperLimit(double meterUpperLimit) {
                this.meterUpperLimit = meterUpperLimit;
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
