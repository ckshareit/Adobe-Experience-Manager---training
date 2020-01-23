function check() {
if(workflowData.getMetaDataMap().get("lastTaskAction","") == "Approved") {
return false
}
return true;
