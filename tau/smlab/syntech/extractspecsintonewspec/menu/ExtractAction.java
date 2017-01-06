package tau.smlab.syntech.extractspecsintonewspec.menu;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import tau.smlab.syntech.extractspecsintonewspec.action.ActionsID;
import tau.smlab.syntech.extractspecsintonewspec.action.ExtractSpecsIntoNewSpecAction;

public class ExtractAction extends ContributionItem{

	public ExtractAction(){
	}
	
    public ExtractAction(String id) {
        super(id);
    }

    @Override
    public void fill(Menu menu, int index) {
        //Here you could get selection and decide what to do
        //You can also simply return if you do not want to show a menu

        //create the menu item
        MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index);
        menuItem.setText("Extract Subset of the Specification into a New Spec");
        menuItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
              System.out.println("marked code " + e.text);
                //what to do when menu is subsequently selected.
                new ExtractSpecsIntoNewSpecAction().run(ActionsID.CREATE);             
            }
        });
    }
}