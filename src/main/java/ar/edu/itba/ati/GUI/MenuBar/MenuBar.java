package ar.edu.itba.ati.GUI.MenuBar;

import ar.edu.itba.ati.Interface.Controller;


public class MenuBar extends javafx.scene.control.MenuBar {

    Controller controller;


    public MenuBar(Controller controller){
        this.controller = controller;

        this.getMenus().addAll(new FileMenu(controller),new GenerateMenu(controller)/*,new SelectionMenu()*/,
                new ToolsMenu(controller), new OperationsMenu(controller), new NoiseMenu(controller),
                new SmoothingMenu(controller));

    }


}
