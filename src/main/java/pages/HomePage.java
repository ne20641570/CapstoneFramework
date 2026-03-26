package pages;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import utils.ExtentReportUtil;
import utils.ExtentTestManager;
import utils.UIActions;
import utils.WaitUtils;

public class HomePage {
    private String title = "BSE (formerly Bombay Stock Exchange) | Live Stock Market updates for BSE SENSEX, Stock Price, Company News & Results";
    private By popUpcloseButton = By.xpath("//h5[@id=\"exampleModalLabel\"]/following-sibling::button[@aria-label=\"Close\"]");
    ExtentTest test;
    public void verifyTitle(ExtentTest tests){
        test=ExtentTestManager.startNode(tests,"Verifying the title");
        ExtentReportUtil.logInfo(test,"Expected title: " + title);
        Assert.assertEquals(UIActions.getTitle(),title);
        ExtentReportUtil.logInfo(test,"Actual title: " + UIActions.getTitle());
    }
    public void closePopup(){
        WaitUtils.explicitWait(popUpcloseButton);
        if(UIActions.isDisplayed(popUpcloseButton)){
            UIActions.click(popUpcloseButton);
        }
    }
}