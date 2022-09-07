package kr.co.seoulit.logistics.logiinfosvc.compinfo.to;

public class ImageTO extends BaseTO{
   
   private String itemGroupCode;
   private String image;
   private String explanation;
   
   public String getItemGroupCode() {
      return itemGroupCode;
   }
   public void setItemGroupCode(String itemGroupCode) {
      this.itemGroupCode = itemGroupCode;
   }
   public String getItemImagelocation() {
      return image;
   }
   public void setImage(String image) {
      this.image = image;
   }
   public String getItemDescription() {
      return explanation;
   }
   public void setExplanation(String explanation) {
      this.explanation = explanation;
   }
   
}