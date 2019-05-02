function drawPlot(boxWidth,boxHeight,overLap)
[X,Y]=main(boxWidth,boxHeight,overLap);
p=1;
q=1;
for i=1:(1-overLap)*boxWidth:512-(overLap)*boxWidth
 for j=1:(1-overLap)*boxWidth:512-(overLap)*boxHeight
   x=i+(boxWidth*0);
   y=j+(boxWidth*0);
   u=X(p,q);
   v=Y(p,q);   
   h=quiver(x,y,u,v,6);
   set(h,'linewidth',1);
   set(h,'MaxHeadSize',3);
   %adjust_quiver_arrowhead_size(h, 1.5);
   if i==1&&j==1
   hold
   end
   q=q+1;
 end
 q=1;
 p=p+1;
end
end